package ecommerce.controller

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
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

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `Returns Products`() {
        create()

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().get<List<Product>>().size).isEqualTo(1)
    }

    @Test
    fun `Returns empty List`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().get<List<Product>>().size).isEqualTo(0)
    }

    @Test
    fun `Returns Product`() {
        create()

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().get<String>("name")).isEqualTo("Product 1")
    }

    @Test
    fun update() {
        create()

        val response =
            RestAssured
                .given().log().all()
                .body(
                    Product(
                        name = "Product 2",
                        price = 10.0,
                        imageUrl = "http://localhost:8080/image/upload/product1.jpg"
                    )
                )
                .contentType(ContentType.JSON)
                .`when`().put("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().get<String>("name")).isEqualTo("Product 2")
    }

    @Test
    fun `Throws NotFoundException on update method if Product not Found`() {
        val response =
            RestAssured
                .given().log().all()
                .body(
                    Product(
                        name = "Product 2",
                        price = 10.0,
                        imageUrl = "http://localhost:8080/image/upload/product1.jpg"
                    )
                )
                .contentType(ContentType.JSON)
                .`when`().put("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun delete() {
        create()

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
    @Test
    fun `Throws NotFoundException on delete method if Product not Found`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().delete("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `Throws NotFoundException on read method if Product not Found`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `Throws NotFoundException if No id provided`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products/")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
