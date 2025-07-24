package ecommerce.controller

import ecommerce.domain.Product
import ecommerce.dto.product.CreateProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.dto.product.UpdateProductRequest
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
        RestAssured
            .given().log().all()
            .body(CreateProductRequest(name = "test", price = 20.0, img = "http://img1", 2))
            .contentType(ContentType.JSON)
            .`when`().post("/api/products")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.CREATED.value())
            .extract()
    }

    @Test
    fun getProducts() {
        createProduct()

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value())
                .extract()

        assertThat(response.jsonPath().getList<Product>("")).hasSize(1)
    }

    @Test
    fun updateProduct() {
        createProduct()

        val response =
            RestAssured
                .given().log().all()
                .body(UpdateProductRequest(name = "test", price = 30.0, img = "http://img1", quantity = 2))
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/1")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value())
                .extract()

        val responseProduct = response.`as`(ProductResponse::class.java)

        assertThat(responseProduct.id).isEqualTo(1)
        assertThat(responseProduct.name).isEqualTo("test")
        assertThat(responseProduct.price).isEqualTo(30.0)
        assertThat(responseProduct.img).isEqualTo("http://img1")
        assertThat(responseProduct.quantity).isEqualTo(2)
    }

    @Test
    fun delete() {
        createProduct()

        RestAssured
            .given().log().all()
            .`when`().delete("/api/products/100")
            .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value())
            .extract()
    }
}
