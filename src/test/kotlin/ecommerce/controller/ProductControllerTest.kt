package ecommerce.controller

import ecommerce.dto.ProductDTO
import ecommerce.dto.ProductPatchDTO
import ecommerce.repository.ProductRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Autowired
    lateinit var productRepository: ProductRepository

    @Test
    fun create() {
        val product =
            ProductDTO(
                name = "ControllerCre",
                price = 10.0,
                imageUrl = "http://localhost:8080/image/upload/product1.jpg",
                description = "Product 1",
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
        val response =
            RestAssured
                .given().log().all()
                .`when`().get("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `Returns Product`() {
        val id = createProduct("Get One Product")
        val response =
            RestAssured
                .given().log().all()
                .`when`().get("/products/$id")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun update() {
        val key = createProduct("Update")
        val response =
            RestAssured
                .given().log().all()
                .body(
                    ProductDTO(
                        name = "Product2",
                        price = 10.0,
                        imageUrl = "http://localhost:8080/image/upload/product1.jpg",
                        description = "Product 1",
                    ),
                )
                .contentType(ContentType.JSON)
                .`when`().put("/products/$key")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun patch() {
        val key = createProduct("Patch")
        val response =
            RestAssured
                .given().log().all()
                .body(
                    ProductPatchDTO(
                        price = 19.0,
                    ),
                )
                .contentType(ContentType.JSON)
                .`when`().patch("/products/$key")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
        val id = createProduct("Delete")
        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/products/$id")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
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

    private fun createProduct(name: String): Long {
        return productRepository.create(
            ProductDTO(
                name = name,
                price = 10.0,
                imageUrl = "url.com",
                description = "description",
            ),
        )
    }
}
