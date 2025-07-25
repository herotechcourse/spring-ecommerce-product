package ecommerce

import ecommerce.dto.CartRequest
import ecommerce.dto.MemberRequest
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartIntegrationTest {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var productRepository: ProductRepository

    private lateinit var token: String
    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        // Register a new member and get token
        val memberRequest = MemberRequest("user@example.com", "password123")

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(memberRequest)
            .post("/api/members/register")
            .then()
            .statusCode(HttpStatus.CREATED.value())

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(memberRequest)
                .post("/api/members/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        token = response.jsonPath().getString("token")

        // Create a product to add to cart
        val productObj = Product(
            id = 0,
            name = "Test Product",
            price = 10.0,
            imageUrl = "https://example.com/image.jpg"
        )
        productRepository.createProduct(productObj)
        product = productRepository.getAll().first()
    }

    @Test
    fun `user can add, get, and remove product in cart`() {
        val cartRequest = CartRequest(productId = product.id)

        // Add to cart
        RestAssured.given()
            .header("Authorization", "Bearer $token")
            .contentType(ContentType.JSON)
            .body(cartRequest)
            .post("/api/cart")
            .then()
            .statusCode(HttpStatus.OK.value())

        // Get cart
        val cartItems = RestAssured.given()
            .header("Authorization", "Bearer $token")
            .get("/api/cart")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("", Map::class.java)

        assertThat(cartItems).hasSize(1)
        assertThat(cartItems[0]["productId"]).isEqualTo(product.id.toInt())

        // Remove from cart
        RestAssured.given()
            .header("Authorization", "Bearer $token")
            .contentType(ContentType.JSON)
            .body(cartRequest)
            .delete("/api/cart")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())

        // Check cart is empty again
        val updatedCartItems = RestAssured.given()
            .header("Authorization", "Bearer $token")
            .get("/api/cart")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("", Map::class.java)

        assertThat(updatedCartItems).isEmpty()
    }
}
