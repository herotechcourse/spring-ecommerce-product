package ecommerce

import ecommerce.dto.CartItemRequest
import ecommerce.dto.TokenRequest
import ecommerce.repository.ProductRepository
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

    private fun deleteProductRequest(cartRequest: CartItemRequest, token: String): ExtractableResponse<Response> {
        val cartResponse =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $token")
                .body(cartRequest).contentType(ContentType.JSON)
                .`when`()
                .delete("/api/cart")
                .then().log().all()
                .extract()
        return cartResponse
    }

    @Test
    fun `test adding multiple valid products to cart`() {
        // login
        val token = login("sandra@email.com", "MyPassword")

        // request add product to cart
        val cartRequest = CartItemRequest(2, 3)
        val cartResponse = addProductRequest(cartRequest, token)

        assertThat(cartResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(cartResponse.body().jsonPath().getString("quantity")).isEqualTo("3")
        assertThat(cartResponse.body().jsonPath().getString("productId")).isEqualTo("2")
    }

    @Test
    fun `test adding invalid product to cart`() {
        // login
        val token = login("simon@email.com", "Hello1234")

        // request add product to cart
        val cartRequest = CartItemRequest(10, 3)
        val cartResponse = addProductRequest(cartRequest, token)

        assertThat(cartResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `test deleting one item from several cart`() {
        // login
        val token = login("simon@email.com", "Hello1234")

        // add products
        val addRequest = CartItemRequest(2, 3)
        addProductRequest(addRequest, token)

        // delete products
        val deleteOneRequest = CartItemRequest(2, 1)
        val deleteOneResponse = deleteProductRequest(deleteOneRequest, token)

        assertThat(deleteOneResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(deleteOneResponse.body().jsonPath().getString("quantity")).isEqualTo("2")
    }

    @Test
    fun `test deleting all items from several in cart`() {
        // login
        val token = login("simon@email.com", "Hello1234")

        // add products
        val addRequest = CartItemRequest(2, 3)
        addProductRequest(addRequest, token)

        // delete products
        val deleteAllRequest = CartItemRequest(2, 3)
        val deleteAllResponse = deleteProductRequest(deleteAllRequest, token)

        assertThat(deleteAllResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `test deleting nonexistent item from cart`() {
        // login
        val token = login("simon@email.com", "Hello1234")

        // delete products
        val deleteNonexistentRequest = CartItemRequest(2, 2)
        val deleteNonExistentResponse = deleteProductRequest(deleteNonexistentRequest, token)

        assertThat(deleteNonExistentResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
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
    fun `test retrieving products`() {
        // login
        val token = login("simon@email.com", "Hello1234")

        // add multiple products
        val product1 = CartItemRequest(1, 3)
        val product2 = CartItemRequest(2, 3)
        val product3 = CartItemRequest(3, 3)
        addProductRequest(product1, token)
        addProductRequest(product2, token)
        addProductRequest(product3, token)

        val  cartResponse = RestAssured.given().log().all()
            .header("Authorization", "Bearer $token")
            .`when`()
            .get("/api/cart")
            .then().log().all()
            .extract()

        assertThat(cartResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        val jsonPath = cartResponse.body().jsonPath()

        assertThat(cartResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(jsonPath.getLong("cartId")).isEqualTo(1L)

        val items: List<String> = jsonPath.getList("items")
        assertThat(items).hasSize(3)

        assertThat(jsonPath.getString("items[0].productName")).isEqualTo("cola")
        assertThat(jsonPath.getInt("items[0].quantity")).isEqualTo(3)
        assertThat(jsonPath.getDouble("items[0].productPrice")).isEqualTo(2.0)
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

