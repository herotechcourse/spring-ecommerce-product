package ecommerce.controller

import ecommerce.dto.ProductRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    fun create() {
        val productRequest =
            ProductRequest(
                name = "Product 1",
                price = 10.0,
                imageUrl = "http://localhost:8080/image/upload/product1.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .post("/products")
                .then()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `Returns Products`() {
        val response =
            RestAssured.given()
                .`when`().get("/products")
                .then()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.asString()).contains("Tablet")
    }

    @Test
    fun update() {
        val productId = 1L
        val updatedProduct =
            ProductRequest(
                name = "Product 2",
                price = 20.0,
                imageUrl = "http://localhost:$port/image/upload/product2.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedProduct)
                .put("/products/$productId")
                .then()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
        create() // create a product first to delete

        val response =
            RestAssured.given()
                .`when`().delete("/products/1")
                .then()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `create fails when product name is blank`() {
        val invalidProduct =
            ProductRequest(
                name = "",
                price = 10.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .post("/products")
                .then()
                .extract()

        println("Response body:\n${response.body()}")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.asString()).contains("Product name must not be blank")
    }

    @Test
    fun `create fails when product name is too long`() {
        val invalidProduct =
            ProductRequest(
                name = "This name is definitely way too long",
                price = 10.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .post("/products")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Invalid characters in product name", "name")
    }

    @Test
    fun `create fails when product name has invalid special chars`() {
        val invalidProduct =
            ProductRequest(
                name = "Invalid@Name!",
                price = 10.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .post("/products")
                .then()
                .extract()
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Invalid characters in product name", "name")
    }

    @Test
    fun `create fails when product price is zero`() {
        val invalidProduct =
            ProductRequest(
                name = "ValidName",
                price = 0.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .post("/products")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Price must be greater than 0", "price")
    }

    @Test
    fun `create fails when product price is negative`() {
        val invalidProduct =
            ProductRequest(
                name = "ValidName",
                price = -5.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .post("/products")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Price must be greater than 0", "price")
    }

    @Test
    fun `create fails when imageUrl does not start with http or https`() {
        val invalidProduct =
            ProductRequest(
                name = "ValidName",
                price = 10.0,
                imageUrl = "ftp://invalid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .post("/products")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Image URL must start with http:// or https://", "imageUrl")
    }

// Now similar tests for update endpoint:

    @Test
    fun `update fails when product name is blank`() {
        val productId = 1L
        val invalidProduct =
            ProductRequest(
                name = "",
                price = 10.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .put("/products/$productId")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Product name must not be blank", "name")
    }

    @Test
    fun `update fails when product price is zero`() {
        val productId = 1L
        val invalidProduct =
            ProductRequest(
                name = "ValidName",
                price = 0.0,
                imageUrl = "http://valid-url.com/image.jpg",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .put("/products/$productId")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Price must be greater than 0", "price")
    }

    @Test
    fun `update fails when imageUrl invalid`() {
        val productId = 1L
        val invalidProduct =
            ProductRequest(
                name = "ValidName",
                price = 10.0,
                imageUrl = "invalid-url",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidProduct)
                .put("/products/$productId")
                .then()
                .extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.asString()).contains("Image URL must start with http:// or https://", "imageUrl")
    }
}
