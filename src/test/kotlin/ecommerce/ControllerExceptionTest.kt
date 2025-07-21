package ecommerce

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ControllerExceptionTest {
    @Test
    fun handleExceptionUsingExceptionHandler() {
        val response =
            RestAssured
                .given().log().all().body(Product(name = "colaaaaaaaaaaaaaaaaaaaaaaaaaaaa", price = 0.0, imageUrl = "abchttps://cola.jpg"))
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body().asString()).contains("Product name cannot exceed 15 characters")
    }

    @Test
    fun sameNameException() {
        val response =
            RestAssured
                .given().log().all().body(Product(name = "cola", price = 4.5, imageUrl = "https://cola.jpg"))
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        val sameNameResponse =
            RestAssured
                .given().log().all().body(Product(name = "cola", price = 4.5, imageUrl = "https://cola.jpg"))
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(sameNameResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(sameNameResponse.headers().toString()).contains("Product with name 'cola' already exists")
    }
}
