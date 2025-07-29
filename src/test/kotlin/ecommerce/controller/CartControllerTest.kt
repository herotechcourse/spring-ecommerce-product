package ecommerce.controller

import ecommerce.auth.JwtTokenProvider
import ecommerce.controller.api.CartController
import ecommerce.dao.JdbcCartDAO
import ecommerce.dao.JdbcMemberDAO
import ecommerce.dao.JdbcProductDAO
import ecommerce.dto.AuthResponse
import ecommerce.dto.CartAddItemForm
import ecommerce.dto.CartUpdateQuantityForm
import ecommerce.dto.LoginForm
import ecommerce.exception.NotFoundException
import ecommerce.model.Member
import ecommerce.service.AuthService
import ecommerce.service.CartService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartControllerTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired private lateinit var jdbcMemberDAO: JdbcMemberDAO

    @Autowired private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var authService: AuthService

    @Autowired private lateinit var jdbcProductDAO: JdbcProductDAO

    @Autowired private lateinit var jdbcCartDAO: JdbcCartDAO

    @Autowired private lateinit var cartService: CartService

    @Autowired private lateinit var controller: CartController

    @BeforeEach
    fun setUp() {
        jdbcMemberDAO = JdbcMemberDAO(jdbcTemplate)
        authService = AuthService(jdbcMemberDAO, jwtTokenProvider)
        jdbcProductDAO = JdbcProductDAO(jdbcTemplate)
        jdbcCartDAO = JdbcCartDAO(jdbcTemplate)
        cartService = CartService(jdbcCartDAO, jdbcProductDAO)

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
                role     VARCHAR(255),
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
            INSERT INTO product (name, price, imageUrl) VALUES ('Batman', 1000, 'https://alexnsan.comics/imageurl/6');
            ;
            
            INSERT INTO member (email, password, role) VALUES ( 'san@htc.com', 'san1234', 'admin');
            INSERT INTO member (email, password, role) VALUES ( 'dan@htc.com', 'dan1234', 'admin');
            INSERT INTO member (email, password) VALUES ( 'ann@htc.com', 'ann1234');
            INSERT INTO member (email, password) VALUES ( 'min@htc.com', 'min1234');
            """
        jdbcTemplate.batchUpdate(query)

        controller = CartController(cartService)
    }

    @Test
    fun addToCart() {
        val productId = PRODUCT_ID
        val quantity = 1
        val form = CartAddItemForm(productId, quantity)
        val expected = CartController.MESSAGE_ADD_SUCCESS
        val response = controller.addToCart(form, LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `addToCart() - return 200 OK when credential is valid`() {
        val productId = PRODUCT_ID
        val quantity = 1

        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(LOGIN_EMAIL, LOGIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $accessToken")
                .body(CartAddItemForm(productId, quantity))
                .contentType(ContentType.JSON)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        val targets = CartController.MESSAGE_ADD_SUCCESS
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.asString()).isIn(targets)
    }

    @Test
    fun `addToCart() - return 401 Unauthorized when credential is invalid`() {
        val productId = PRODUCT_ID
        val quantity = 1

        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(LOGIN_EMAIL, LOGIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        val contaminatedToken = accessToken + 123
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $contaminatedToken")
                .body(CartAddItemForm(productId, quantity))
                .contentType(ContentType.JSON)
                .`when`().post("/api/cart")
                .then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Form validation failure when 'productId' is blank`() {
        val productId = 0L
        val quantity = 1
        val response =
            RestAssured
                .given().log().all()
                .body(CartAddItemForm(productId, quantity))
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
                .body(CartAddItemForm(productId, quantity))
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
        val response = controller.viewCart(LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(expected)
    }

    @Test
    fun `viewCart() - 1 item in cart`() {
        addToCart()
        val expected = 1
        val response = controller.viewCart(LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(expected)
    }

    @Test
    fun `removeFromCart() - return 200 OK when remove success`() {
        addToCart()
        val productId = PRODUCT_ID
        val expected = CartController.MESSAGE_REMOVE_SUCCESS
        val response = controller.removeFromCart(productId, LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `removeFromCart() - throws exception when nothing to remove`() {
        val productId = PRODUCT_ID
        assertThrows<NotFoundException> { controller.removeFromCart(productId, LOGIN_MEMBER) }
    }

    @Test
    fun `updateQuantity() - return 200 OK when update success`() {
        addToCart()
        val productId = PRODUCT_ID
        val quantity = 10
        val form = CartUpdateQuantityForm(quantity)
        val expected = CartController.MESSAGE_UPDATE_SUCCESS
        val response = controller.updateQuantity(productId, form, LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `updateQuantity() - throws exception when nothing to update`() {
        val productId = PRODUCT_ID
        val quantity = 10
        val form = CartUpdateQuantityForm(quantity)
        assertThrows<NotFoundException> { controller.updateQuantity(productId, form, LOGIN_MEMBER) }
    }

    @Test
    fun `Interceptor - allow admin user to access admin endpoint`() {
        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(LOGIN_EMAIL, LOGIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $accessToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/cart-stats/top5-products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `Interceptor - block non-admin user to access admin endpoint`() {
        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(NON_ADMIN_EMAIL, NON_ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $accessToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/cart-stats/top5-products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    companion object {
        private const val PRODUCT_ID = 1L
        private const val LOGIN_EMAIL = "san@htc.com"
        private const val LOGIN_PASSWORD = "san1234"
        private const val NON_ADMIN_EMAIL = "min@htc.com"
        private const val NON_ADMIN_PASSWORD = "min1234"
        private val LOGIN_MEMBER = Member(1L, LOGIN_EMAIL, LOGIN_PASSWORD)
    }
}
