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
    fun createProduct() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(id = 100, name = "test", price = 20.0, img = "http://img1", 2))
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun getProducts() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList<Product>("")).hasSize(3)
    }

    @Test
    fun updateProduct() {
        createProduct()

        val response =
            RestAssured
                .given().log().all()
                .body(Product(id = 100, name = "test", price = 30.0, img = "http://img1", quantity = 2))
                .contentType(ContentType.JSON)
                .`when`().put("/products/100")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
        val initialProductName =
            """
            {
            "name" : "ProductName",
            "price": 10.0,
            "img": "http://valid_image_url.com",
            "quantity": 10
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(initialProductName).post("/products").then()
            .statusCode(201)

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
}
