package ecommerce

import ecommerce.dto.CartItemRequest
import ecommerce.dto.TokenRequest
import ecommerce.repository.ProductRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartE2ETest {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE IF EXISTS cart_items")
        jdbcTemplate.execute("DROP TABLE IF EXISTS carts")
        jdbcTemplate.execute("DROP TABLE IF EXISTS products")
        jdbcTemplate.execute("DROP TABLE members IF EXISTS")

        createMembersTable()
        createProductsTable()
        jdbcTemplate.execute(
            "CREATE TABLE carts(" + " cart_id SERIAL, user_id INT UNIQUE)",
        )
        jdbcTemplate.execute(
            "CREATE TABLE cart_items(cart_id INT, product_id INT, quantity INT )",
        )
    }

    private fun createMembersTable() {
        jdbcTemplate.execute(
            "CREATE TABLE members(" + " id SERIAL, email VARCHAR(20) UNIQUE, password VARCHAR(50), role VARCHAR(10))",
        )

        val splitUpAttributes: List<Array<String>> =
            listOf(
                "sandra@email.com MyPassword user",
                "simon@email.com Hello1234 user",
                "sara@email.com 1234567! user",
            ).map { name -> name.split(" ").toTypedArray() }.toList()
        jdbcTemplate.batchUpdate("INSERT INTO members(email, password, role) VALUES (?,?,?)", splitUpAttributes)
    }

    private fun createProductsTable() {
        jdbcTemplate.execute(
            "CREATE TABLE products(" + "id SERIAL, name VARCHAR(100), price DECIMAL(10,2), image_url VARCHAR(500))",
        )

        val splitUpAttributes: List<Array<String>> =
            listOf(
                "cola 2 http//cola",
                "fanta 3 http//fanta",
                "coffee 4 http//coffee",
            ).map { name -> name.split(" ").toTypedArray() }.toList()
        jdbcTemplate.batchUpdate("INSERT INTO products(name, price, image_url) VALUES (?,?,?)", splitUpAttributes)

    }

    @Test
    fun `test adding valid products to cart`() {
        // login
        val loginRequest = TokenRequest(email = "simon@email.com", password = "Hello1234")
        val loginResponse =
            RestAssured.given().log().all().body(loginRequest).contentType(ContentType.JSON).`when`()
                .post("/api/members/login").then().log().all().extract()
        val token = loginResponse.body().jsonPath().getString("token")

        // request add product to cart
        val cartRequest = CartItemRequest(2, 3)
        val cartResponse =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $token")
                .body(cartRequest).contentType(ContentType.JSON)
                .`when`()
                .post("/api/cart")
                .then().log().all()
                .extract()
        assertThat(cartResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value())

    }

    @Test
    fun `test cart request with invalid token`() {
        val token = "ndwndwoljdwpfkwkdsq.DNlwfk3wld'wamclwfjkepojfo3jf"
        val tokenResponse =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $token")
                .`when`()
                .get("/api/cart")
                .then().log().all()
                .extract()

        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `test cart request without 'Authorization' header`() {
        val loginRequest = TokenRequest(email = "sara@email.com", password = "1234567!")
        val loginResponse =
            RestAssured.given().log().all()
                .body(loginRequest).contentType(ContentType.JSON)
                .`when`()
                .post("/api/members/login")
                .then().log().all()
                .extract()

        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value())

        val token = loginResponse.body().jsonPath().getString("token")
        val tokenResponse =
            RestAssured.given().log().all()
                .header("Location", "Bearer $token")
                .`when`()
                .get("/api/cart")
                .then().log().all()
                .extract()

        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}

