package ecommerce.controller

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
    @Test
    fun create() {
        val product = Product(
            name = "Product 1",
            price = 10.0,
            imageUrl = "http://localhost:8080/image/upload/product1.jpg"
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
}
