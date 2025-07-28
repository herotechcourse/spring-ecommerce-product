package ecommerce

import ecommerce.auth.security.JwtProvider
import ecommerce.cart.dto.CartRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WishControllerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var jwtProvider: JwtProvider
    private lateinit var validToken: String

    @BeforeEach
    fun setUp() {
        jwtProvider =
            JwtProvider(
                secret = "3GXmjHcKG7ww13vbHu3aMdz+Q25wKxiGAeV43Tc3qsA=",
                validityInMilliseconds = 3600000,
            )
        validToken = jwtProvider.createToken("test@example.com")

        jdbcTemplate.execute("DROP TABLE CART_ITEMS IF EXISTS")
        jdbcTemplate.execute("DROP TABLE PRODUCTS IF EXISTS")
        jdbcTemplate.execute("DROP TABLE MEMBERS IF EXISTS")
        jdbcTemplate.execute(
            """
            CREATE TABLE MEMBERS (
                id SERIAL PRIMARY KEY,
                email VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(50) NOT NULL
            )
            """,
        )
        jdbcTemplate.execute(
            """
            CREATE TABLE PRODUCTS (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                image_url VARCHAR(500) NOT NULL
            )
            """,
        )
        jdbcTemplate.execute(
            """
            CREATE TABLE CART_ITEMS (
                id SERIAL PRIMARY KEY,
                member_id BIGINT NOT NULL,
                product_id BIGINT NOT NULL,
                FOREIGN KEY (member_id) REFERENCES MEMBERS(id),
                FOREIGN KEY (product_id) REFERENCES PRODUCTS(id),
                UNIQUE (member_id, product_id)
            )
            """,
        )

        jdbcTemplate.update(
            "INSERT INTO MEMBERS (email, password, role) VALUES (?, ?, ?)",
            "test@example.com",
            "password123",
            "USER",
        )
        val products =
            listOf(
                arrayOf("Cola", 2.52, "https://t4.ftcdn.net/jpg/02/84/65/61/360_F_284656117_sPF8gVWaX627bq5qKrlrvCz1eFfowdBf.jpg"),
                arrayOf("Fanta", 2.50, "https://www.cokesolutions.com/content/dam/cokesolutions/us/images/Products/Fanta-Orange-PET.jpg"),
            )
        jdbcTemplate.batchUpdate(
            "INSERT INTO PRODUCTS (name, price, image_url) VALUES (?, ?, ?)",
            products,
        )
    }

    @Test
    fun `addToCart should return created cart item`() {
        val request = CartRequest(productId = 1)

        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer $validToken")
                .body(request)
                .`when`()
                .post("/api/cart-items")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.jsonPath().getLong("id")).isEqualTo(1)
        assertThat(response.jsonPath().getLong("product.id")).isEqualTo(1)
        assertThat(response.jsonPath().getString("product.name")).isEqualTo("Cola")
        assertThat(response.jsonPath().getDouble("product.price")).isEqualTo(2.52)
    }

    @Test
    fun `addToCart should return 401 for invalid token`() {
        val request = CartRequest(productId = 1)

        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer invalid-token")
                .body(request)
                .`when`()
                .post("/api/cart-items")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Unauthorized: Invalid or expired JWT token")
    }

    @Test
    fun `addToCart should return 400 for non-existent product`() {
        val request = CartRequest(productId = 999)

        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer $validToken")
                .body(request)
                .`when`()
                .post("/api/cart-items")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Product not found")
    }

    @Test
    fun `addToCart should return 400 for duplicate product in cart`() {
        jdbcTemplate.update(
            "INSERT INTO CART_ITEMS (member_id, product_id) VALUES (?, ?)",
            1,
            1,
        )

        val request = CartRequest(productId = 1)

        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer $validToken")
                .body(request)
                .`when`()
                .post("/api/cart-items")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Product is already in the cart")
    }

    @Test
    fun `getCartItems should return list of cart items`() {
        jdbcTemplate.update(
            "INSERT INTO CART_ITEMS (member_id, product_id) VALUES (?, ?)",
            1,
            1,
        )

        val response =
            RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer $validToken")
                .`when`()
                .get("/api/cart-items")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList<Any>("").size).isEqualTo(1)
        assertThat(response.jsonPath().getLong("[0].id")).isEqualTo(1)
        assertThat(response.jsonPath().getLong("[0].product.id")).isEqualTo(1)
        assertThat(response.jsonPath().getString("[0].product.name")).isEqualTo("Cola")
        assertThat(response.jsonPath().getDouble("[0].product.price")).isEqualTo(2.52)
    }

    @Test
    fun `getCartItems should return 401 for invalid token`() {
        val response =
            RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer invalid-token")
                .`when`()
                .get("/api/cart-items")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Unauthorized: Invalid or expired JWT token")
    }

    @Test
    fun `removeFromCart should return 204 for successful deletion`() {
        jdbcTemplate.update(
            "INSERT INTO CART_ITEMS (member_id, product_id) VALUES (?, ?)",
            1,
            1,
        )

        val response =
            RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer $validToken")
                .`when`()
                .delete("/api/cart-items/1")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `removeFromCart should return 401 for invalid token`() {
        val response =
            RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer invalid-token")
                .`when`()
                .delete("/api/cart-items/1")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Unauthorized: Invalid or expired JWT token")
    }

    @AfterEach
    fun tearDown() {
        jdbcTemplate.execute("DELETE FROM CART_ITEMS")
        jdbcTemplate.execute("DELETE FROM PRODUCTS")
        jdbcTemplate.execute("DELETE FROM MEMBERS")
        jdbcTemplate.execute("ALTER TABLE MEMBERS ALTER COLUMN id RESTART WITH 1")
        jdbcTemplate.execute("ALTER TABLE PRODUCTS ALTER COLUMN id RESTART WITH 1")
    }
}
