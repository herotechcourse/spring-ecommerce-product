package ecommerce

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerExceptionTest {
    private fun makeRequestToProducts(product: Product): ExtractableResponse<Response> =
        RestAssured
            .given().log().all().body(product)
            .contentType(ContentType.JSON)
            .`when`().post("/products")
            .then().log().all().extract()

    @Test
    fun handleExceptionUsingExceptionHandler() {
        val response =
            makeRequestToProducts(
                Product(
                    name = "colaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    price = 0.0,
                    imageUrl = "abchttps://cola.jpg",
                ),
            )

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body().asString()).contains("Product name cannot exceed 15 characters")
    }

    @Test
    fun sameNameException() {
        val response = makeRequestToProducts(Product(name = "cola", price = 4.5, imageUrl = "https://cola.jpg"))
        val sameNameResponse = makeRequestToProducts(Product(name = "cola", price = 4.5, imageUrl = "https://cola.jpg"))

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(sameNameResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(sameNameResponse.body().asString()).contains("Product with name cola already exists")
    }
}
