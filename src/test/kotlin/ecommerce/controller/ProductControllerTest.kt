package ecommerce.controller

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    companion object {
        fun createProducts(): MutableSet<Product> {
            val product1 = Product(1L, "cafe", 39.00, "www.test")
            val product2 = Product(2L, "table", 39.00, "www.test")
            val product3 = Product(3L, "chair", 39.00, "www.test")
            val products =
                RestAssured
                    .given().log().all().body(product1)
                    .contentType(ContentType.JSON)
                    .`when`()
                    .request("POST", "/products")
                    .then()
                    .extract()
                    .response()
            RestAssured
                .given().log().all().body(product2)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/products")
                .then()
                .extract()
                .response()
            RestAssured
                .given().log().all().body(product3)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/products")
                .then()
                .extract()
                .response()
            return mutableSetOf(product1, product2, product3)
        }
    }

    @Test
    fun addProduct() {
        val product = Product(1L, "cafe", 39.00, "www.test")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/products")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(response.`as`(Product::class.java).name).isEqualTo("cafe")
        assertThat(response.`as`(Product::class.java).price).isEqualTo(39.00)
        assertThat(response.`as`(Product::class.java).imageUrl).isEqualTo("www.test")
    }

    @Test
    fun getProducts() {
        val products = createProducts()
        val response =
            RestAssured
                .given().log().all()
                .`when`()
                .request("GET", "/products")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body.jsonPath().getList<Product>("")).hasSize(3)
    }

    @Test
    fun getProduct() {
        val product = Product(1L, "cafe", 39.00, "www.test")
        val products = createProducts()
        val response =
            RestAssured
                .given().log().all()
                .`when`()
                .request("GET", "/products/1")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(response.`as`(Product::class.java)).isEqualTo(product)
    }
}
