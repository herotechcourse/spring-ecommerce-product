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
    @Test
    fun addProduct() {
        val product = Product("cafe", 39.00, "www.test")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/product")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(response.`as`(Product::class.java).name).isEqualTo("cafe")
        assertThat(response.`as`(Product::class.java).price).isEqualTo(39.00)
        assertThat(response.`as`(Product::class.java).imageUrl).isEqualTo("www.test")
    }
}
