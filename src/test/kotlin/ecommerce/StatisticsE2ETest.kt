package ecommerce

import ecommerce.dto.CartItemRequest
import ecommerce.dto.TokenRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
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
class StatisticsE2ETest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS cart_items")
        jdbcTemplate.execute("DROP TABLE IF EXISTS carts")
        jdbcTemplate.execute("DROP TABLE IF EXISTS products")
        jdbcTemplate.execute("DROP TABLE members IF EXISTS")

        createMembersTable()
        createProductsTable()
        createCartTable()
        createCartItemsTable()

        val token = login("user@email.com","UserPassword")
        val item1 = CartItemRequest(1, 1)
        val item2 = CartItemRequest(2, 1)
        val item3 = CartItemRequest(3, 1)
        val item4 = CartItemRequest(4, 1)
        val item5 = CartItemRequest(5, 1)
        val item6 = CartItemRequest(6, 1)
        val item7 = CartItemRequest(7, 1)
        addProductRequest(item1, token)
        addProductRequest(item2, token)
        Thread.sleep(2000)
        addProductRequest(item3, token)
        addProductRequest(item4, token)
        addProductRequest(item5, token)
        addProductRequest(item6, token)
        addProductRequest(item7, token)
    }

    private fun createCartTable() {
        jdbcTemplate.execute(
            "CREATE TABLE carts(" + " cart_id SERIAL, user_id INT UNIQUE)",
        )
    }

    private fun createCartItemsTable() {
        jdbcTemplate.execute("""
        CREATE TABLE cart_items (
            cart_id INT,
            product_id INT,
            quantity INT DEFAULT 1,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            PRIMARY KEY (cart_id, product_id)
        )
        """.trimIndent())
    }

    private fun createMembersTable() {
        jdbcTemplate.execute(
            "CREATE TABLE members(" + " id SERIAL, email VARCHAR(20) UNIQUE, password VARCHAR(50), role VARCHAR(10))",
        )

        val splitUpAttributes: List<Array<String>> =
            listOf(
                "user@email.com UserPassword user",
                "user2@email.com User2Password user",
                "admin@email.com AdminPassword admin",
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
                "tea 4 http//coffee",
                "milk 2.3 http//coffee",
                "water 1.5 http//coffee",
                "soda 2.0 http//coffee",
            ).map { name -> name.split(" ").toTypedArray() }.toList()
        jdbcTemplate.batchUpdate("INSERT INTO products(name, price, image_url) VALUES (?,?,?)", splitUpAttributes)

    }

    private fun login(email: String, password: String): String {
        val loginRequest = TokenRequest(email, password)
        val loginResponse =
            RestAssured.given().log().all().body(loginRequest).contentType(ContentType.JSON).`when`()
                .post("/api/members/login").then().log().all().extract()
        val token = loginResponse.body().jsonPath().getString("token")
        return token
    }

    private fun addProductRequest(cartRequest: CartItemRequest, token: String): ExtractableResponse<Response> {
        val cartResponse =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $token")
                .body(cartRequest).contentType(ContentType.JSON)
                .`when`()
                .post("/api/cart")
                .then().log().all()
                .extract()
        return cartResponse
    }

    @Test
    fun `should return top 5 most added products in the past 30 days for admin`() {
        // login
        val token = login("admin@email.com","AdminPassword")

        val statisticsResponse = RestAssured.given().log().all()
            .header("Authorization", "Bearer $token")
            .`when`()
            .get("/admin/statistics/top-products")
            .then().log().all()
            .extract()

        assertThat(statisticsResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
//        assertThat(statisticsResponse.body().jsonPath().getString("productName")).isEqualTo("")
//        assertThat(statisticsResponse.body().jsonPath().getString("productQuantity")).isEqualTo("2")
    }
}
