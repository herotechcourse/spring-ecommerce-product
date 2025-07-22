package ecommerce.controller

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForClassTypes
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
        val product =
            Product(
                name = "Product 1",
                price = 10.0,
                imageUrl = "http://localhost:8080/image/upload/product1.jpg",
            )

        val response =
            RestAssured
                .given().log().all()
                .body(product)
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `Returns Products`() {
        val response =
            RestAssured
                .given().log().all()
                .`when`().get("/products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        AssertionsForClassTypes.assertThat(response.asString()).contains("Tablet")
    }

    @Test
    fun update() {
        val productId = 1
        val updatedProduct =
            Product(
                name = "Product 2",
                price = 20.0,
                imageUrl = "http://localhost:$port/image/upload/product2.jpg",
            )

        val updateResponse =
            RestAssured
                .given().log().all()
                .body(updatedProduct)
                .contentType(ContentType.JSON)
                .`when`().put("/products/$productId")
                .then().log().all().extract()

        Assertions.assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
        create()

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/products/1")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `Throws NotFoundException if No id provided`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products/")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
