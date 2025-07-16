package ecommerce.product

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.net.URI

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Test
    fun `should be able return 'created 201' response`() {
        val response =
            RestAssured
                .given().log().all()
                .body(AMERICANO)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `should be able to read a product and return 'ok 200' response`() {
        createProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", Product::class.java)).hasSize(1)
    }

    @Test
    fun `should be able to read products, and return 'ok 200' response`() {
        createProduct(AMERICANO)
        createProduct(FLAT_WHITE)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", Product::class.java)).hasSize(2)
    }

    @Test
    fun `should return 'no-content 204' response`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", Product::class.java)).hasSize(2)
    }

    @Test
    fun `should return 'not found 404' response, when failed to update product`() {
        val response =
            RestAssured
                .given().log().all()
                .body(FLAT_WHITE)
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should be able to delete product, and return '204' response`() {
        createProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
}
