package ecommerce.controller

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.dto.product.ProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.model.Product
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
class ProductControllerTest {
    @LocalServerPort
    private var port: Int = 0
    private lateinit var authToken: String

    @BeforeEach
    fun setupAuthentication() {
        RestAssured.port = port
        val registerRequest = RegisterRequest("validEmail5@email.com", "SecureP@ss1", "name")
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
            .`when`().post("/api/members/register")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()

        val loginRequest = LoginRequest("validEmail5@email.com", "SecureP@ss1")
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

    private fun createProductWithAuth(
        id: Long,
        name: String,
        price: Double,
        img: String,
        quantity: Int,
    ): ProductResponse {
        val createRequest = ProductRequest(name = name, price = price, img = img, quantity = quantity)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(createRequest) // Send the DTO
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        val locationHeader = response.header("Location")
        assertThat(locationHeader).isNotNull()
        val createdProductId = locationHeader.substringAfterLast("/").toLong()

        return RestAssured.given()
            .header("Authorization", "Bearer $authToken")
            .contentType(ContentType.JSON)
            .`when`().get("/products/$createdProductId")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getObject("", ProductResponse::class.java)
    }

    @Test
    fun `createProduct should return 201`() {
        val createRequest = ProductRequest("Test Product10", 10.0, "http://example_img", 2)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .body(createRequest)
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `getProducts should return 200 OK`() {
        createProductWithAuth(210, "ProductK", 10.0, "https://example.com/imgA", 5)
        createProductWithAuth(212, "ProductL", 20.0, "https://example.com/imgB", 3)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .`when`().get("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList<Product>("")).hasSize(5)
    }

    @Test
    fun `updateProduct should return 200 OK`() {
        val createdProduct = createProductWithAuth(900, "original", 20.0, "https://example.com/original", 5)
        val productIdToUpdate = createdProduct.id
        val updateRequest = ProductRequest("updated", 30.0, "https://example.com/original", 5)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .body(updateRequest)
                .contentType(ContentType.JSON)
                .`when`().put("/products/$productIdToUpdate")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val responseBody = response.jsonPath().getObject("", ProductResponse::class.java)
        assertThat(responseBody.name).isEqualTo("updated")
        assertThat(responseBody.price).isEqualTo(30.0)
    }

    @Test
    fun `deleteProduct should return 204 No Content with authentication`() {
        val createdProduct = createProductWithAuth(800, "productToDelete", 20.0, "https://example.com/original", 5)
        val productToDeleteId = createdProduct.id

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .`when`().delete("/products/$productToDeleteId")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
        RestAssured
            .given()
            .header("Authorization", "Bearer $authToken")
            .`when`().get("/products/$productToDeleteId")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }
}
