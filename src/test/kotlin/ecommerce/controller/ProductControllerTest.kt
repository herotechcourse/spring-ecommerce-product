package ecommerce.controller

import ecommerce.auth.JwtProvider
import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Test
    fun `create() should be able return 'created 201' response`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val requestBody =
            mapOf(
                "name" to "Test Product",
                "price" to 9.99,
                "imageUrl" to "https://example.com/product.png",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        val responseJson = response.jsonPath()

        assertThat(responseJson.getString("name")).isEqualTo("Test Product")
        assertThat(responseJson.getFloat("price")).isEqualTo(9.99f)
        assertThat(responseJson.getString("imageUrl")).isEqualTo("https://example.com/product.png")
        assertThat(responseJson.getLong("id")).isNotNull()
    }

    @Test
    fun `create() should be able return '400' when the body request is missing some parameter`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val requestBody =
            mapOf(
                "price" to 9.99,
                "imageUrl" to "https://example.com/product.png",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `create() should return '400' when name length is greater than 15`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val requestBody =
            mapOf(
                "name" to "Test Product With Big Name",
                "price" to 9.99,
                "imageUrl" to "https://example.com/product.png",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `create() should be able return '400' when price is less or equal to 0`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val requestBody =
            mapOf(
                "name" to "Test Product",
                "price" to 0.00,
                "imageUrl" to "https://example.com/product.png",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `create() should return '403' response when user is not authorized to access the resource`() {
        val token = JwtProvider.generateToken("user@test.com")

        val requestBody =
            mapOf(
                "name" to "Test Product",
                "price" to 9.99,
                "imageUrl" to "https://example.com/product.png",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun `read() should be able to read a product and return 'ok 200' response`() {
        // data.sql already have 3 products

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", Product::class.java)).hasSize(5)
    }

    @Test
    fun `update() should be able to update product, and return 'ok 200' response`() {
        val token = JwtProvider.generateToken("admin@test.com")
        val requestBody =
            mapOf(
                "product" to
                    mapOf(
                        "name" to "Flat White",
                        "price" to 3.5,
                        "imageUrl" to "http://example.com/flat-white.jpg",
                    ),
            )

        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().patch("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `update() should return 'not found 404' response, when failed to update product`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val requestBody =
            mapOf(
                "product" to
                    mapOf(
                        "name" to "Flat White",
                        "price" to 3.5,
                        "imageUrl" to "http://example.com/flat-white.jpg",
                    ),
            )

        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().patch("/api/products/99")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `delete() should be able to delete product, and return '204' response`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().delete("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `delete() should return 'not found 404' response, when product id not found`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .`when`().delete("/api/products/99")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
