package ecommerce.controller

import ecommerce.auth.JwtTokenProvider
import ecommerce.controller.api.CartController
import ecommerce.dao.JdbcCartDAO
import ecommerce.dao.JdbcMemberDAO
import ecommerce.dto.CartForm
import ecommerce.exception.NotFoundException
import ecommerce.service.AuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartControllerTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired private lateinit var jdbcMemberDAO: JdbcMemberDAO

    @Autowired private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var authService: AuthService

    @Autowired private lateinit var jdbcCartDAO: JdbcCartDAO
    private lateinit var controller: CartController

    @BeforeEach
    fun setUp() {
        jdbcMemberDAO = JdbcMemberDAO(jdbcTemplate)
        jwtTokenProvider = JwtTokenProvider()
        authService = AuthService(jdbcMemberDAO, jwtTokenProvider)
        jdbcCartDAO = JdbcCartDAO(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE product CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE product 
            (
                id          LONG    NOT NULL AUTO_INCREMENT,
                name        varchar(255)    NOT NULL,
                price       DOUBLE  NOT NULL,
                imageUrl    TEXT    NOT NULL,
                PRIMARY KEY (id)
            );""",
        )

        jdbcTemplate.execute("DROP TABLE member CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE member
            (
                id       LONG         NOT NULL AUTO_INCREMENT,
                email    VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            );""",
        )

        jdbcTemplate.execute("DROP TABLE cart_item CASCADE;")
        jdbcTemplate.execute(
            """CREATE TABLE cart_item
            (
                id         LONG NOT NULL AUTO_INCREMENT,
                member_id  LONG NOT NULL,
                product_id LONG NOT NULL,
                quantity   INT DEFAULT 1,
                PRIMARY KEY (id),
    
                FOREIGN KEY (member_id) REFERENCES member (id),
                FOREIGN KEY (product_id) REFERENCES product (id)
            );""",
        )

        val query =
            """INSERT INTO product (name, price, imageUrl) VALUES ('Iron Man', 1000, 'https://alexnsan.comics/imageurl/1');
            INSERT INTO product (name, price, imageUrl) VALUES ('X-men', 1000, 'https://alexnsan.comics/imageurl/2');
            INSERT INTO product (name, price, imageUrl) VALUES ('Superman', 1000, 'https://alexnsan.comics/imageurl/3');
            INSERT INTO product (name, price, imageUrl) VALUES ('Naruto', 1000, 'https://alexnsan.comics/imageurl/4');
            INSERT INTO product (name, price, imageUrl) VALUES ('Full Metal Alchemist', 1000, 'https://alexnsan.comics/imageurl/5');
            
            INSERT INTO member (email, password) VALUES ( 'san@htc.com', 'san1234');
            INSERT INTO member (email, password) VALUES ( 'dan@htc.com', 'dan1234');
            """
        jdbcTemplate.batchUpdate(query)

        controller = CartController(jdbcCartDAO, authService)
    }

    @Test
    fun addToCart() {
        val productId = PRODUCT_ID
        val quantity = 1
        val form = CartForm(productId, quantity)
        val expected = CartController.MESSAGE_ADD_SUCCESS
        val response = controller.addToCart(form)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `Form validation failure when 'productId' is blank`() {
        val productId = 0L
        val quantity = 1
        val response =
            RestAssured
                .given().log().all()
                .body(CartForm(productId, quantity))
                .contentType(ContentType.JSON)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        val targets =
            listOf(
                "Product ID is missing",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["productId"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `Form validation failure when 'quantity' is less than 1`() {
        val productId = PRODUCT_ID
        val quantity = 0
        val response =
            RestAssured
                .given().log().all()
                .body(CartForm(productId, quantity))
                .contentType(ContentType.JSON)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        val targets =
            listOf(
                "Product quantity is too small",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["quantity"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `viewCart() - empty cart`() {
        val expected = 0
        val response = controller.viewCart()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(expected)
    }

    @Test
    fun `viewCart() - 1 item in cart`() {
        addToCart()
        val expected = 1
        val response = controller.viewCart()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(expected)
    }

    @Test
    fun `removeFromCart() - return 200 OK when remove success`() {
        addToCart()
        val productId = PRODUCT_ID
        val expected = CartController.MESSAGE_REMOVE_SUCCESS
        val response = controller.removeFromCart(productId)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `removeFromCart() - throws exception when nothing to remove`() {
        val productId = PRODUCT_ID
        assertThrows<NotFoundException> { controller.removeFromCart(productId) }
    }

    @Test
    fun `updateQuantity() - return 200 OK when update success`() {
        addToCart()
        val productId = PRODUCT_ID
        val quantity = 10
        val form = CartForm(productId, quantity)
        val expected = CartController.MESSAGE_UPDATE_SUCCESS
        val response = controller.updateQuantity(productId, form)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `updateQuantity() - throws exception when nothing to update`() {
        val productId = PRODUCT_ID
        val quantity = 10
        val form = CartForm(productId, quantity)
        assertThrows<NotFoundException> { controller.updateQuantity(productId, form) }
    }

    companion object {
        private const val PRODUCT_ID = 1L
    }
}
