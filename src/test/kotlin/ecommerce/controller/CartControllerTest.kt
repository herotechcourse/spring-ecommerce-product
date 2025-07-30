package ecommerce.controller

import ecommerce.dto.cartItem.AddCartItemRequest
import ecommerce.dto.cartItem.CartItemResponse
import ecommerce.dto.cartItem.CartResponse
import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.dto.product.ProductRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartControllerTest {
    @LocalServerPort
    private var port: Int = 0
    private lateinit var authToken: String

    @BeforeEach
    fun setupAuthentication() {
        RestAssured.port = port
        val registerRequest = RegisterRequest("validEmail3@email.com", "SecureP@ss1", "name")
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
            .`when`().post("/api/members/register")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()

        val loginRequest = LoginRequest("validEmail3@email.com", "SecureP@ss1")
        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        authToken = response.jsonPath().getString("token")
        assertThat(authToken).isNotNull().isNotBlank()
    }

    private fun createProductWithAuth(createRequest: ProductRequest): Long {
        val response =
            RestAssured
                .given()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(createRequest)
                .`when`().post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()

        val locationHeader = response.header("Location")
        assertThat(locationHeader).isNotNull()
        return locationHeader.substringAfterLast("/").toLong()
    }

    @Test
    fun `add product to Cart should return 200 OK`() {
        val productId =
            createProductWithAuth(
                ProductRequest(
                    "ProductA",
                    10.0,
                    "http://productA_img.jpg",
                    20,
                ),
            )
        val addCartItemRequest = AddCartItemRequest(productId, 2)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(addCartItemRequest)
                .`when`().post("/api/cart/items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        val cartItemResponse = response.jsonPath().getObject("", CartItemResponse::class.java)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(cartItemResponse).isNotNull
        assertThat(cartItemResponse.productId).isEqualTo(productId)
    }

    @Test
    fun `update product in cart should return 200 OK`() {
        val productId =
            createProductWithAuth(
                ProductRequest(
                    "ProductZ",
                    10.0,
                    "http://productA_img.jpg",
                    20,
                ),
            )
        val addCartItemRequest = AddCartItemRequest(productId, 2)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(addCartItemRequest)
                .`when`().post("/api/cart/items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val updateCartItemRequest = AddCartItemRequest(productId, 10)
        val updateResponse =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(updateCartItemRequest)
                .`when`().post("/api/cart/items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        val cartItemResponse = updateResponse.jsonPath().getObject("", CartItemResponse::class.java)
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(cartItemResponse.productId).isEqualTo(productId)
        assertThat(cartItemResponse.quantity).isEqualTo(addCartItemRequest.quantity + updateCartItemRequest.quantity)
        assertThat(cartItemResponse.productName).isEqualTo("ProductZ")
        assertThat(cartItemResponse.productPrice).isEqualTo(10.0)
    }

    @Test
    fun `add non-existent product to cart return 404`() {
        val addNonExistentItem = AddCartItemRequest(100, 2)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(addNonExistentItem)
                .`when`().post("/api/cart/items")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `get cart items from empty cart`() {
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/cart/items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val cartResponse = response.jsonPath().getObject("", CartResponse::class.java)
        assertThat(cartResponse).isNotNull
        assertThat(cartResponse.totalItems).isEqualTo(0)
        assertThat(cartResponse.totalPrice).isEqualTo(0.0)
    }

    @Test
    fun `get cart items from cart with items should return 200 OK with items`() {
        val productPrice = 15.5
        val productQuantity = 5
        val productId =
            createProductWithAuth(
                ProductRequest(
                    "ProductY",
                    productPrice,
                    "http://productB_img.jpg",
                    25,
                ),
            )

        val addCartItemRequest = AddCartItemRequest(productId, productQuantity)
        RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $authToken")
            .contentType(ContentType.JSON)
            .body(addCartItemRequest)
            .`when`().post("/api/cart/items")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .`when`().get("/api/cart/items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val cartResponse = response.jsonPath().getObject("", CartResponse::class.java)
        assertThat(cartResponse).isNotNull
        assertThat(cartResponse.totalItems).isEqualTo(productQuantity)
        assertThat(cartResponse.totalPrice).isEqualTo(productPrice * productQuantity)
    }

    @Test
    fun `delete non existent product from cart should return 404 NOT FOUND`() {
        val nonExistentProductId = 100
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .`when`().delete("/api/cart/items/{productId}", nonExistentProductId)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        val errorResponse = response.jsonPath().getMap<String, String>("")
        assertThat(errorResponse).containsKey("error")
        assertThat(errorResponse["error"]).contains("Product with ID $nonExistentProductId not found in cart for member.")
    }
}
