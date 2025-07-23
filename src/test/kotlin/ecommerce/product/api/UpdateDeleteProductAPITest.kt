package ecommerce.product.api

import ecommerce.TextFixture.AssertTemplate.assertProductEquals
import ecommerce.TextFixture.ValidRequest.AMERICANO
import ecommerce.TextFixture.ValidRequest.FLAT_WHITE
import ecommerce.TextFixture.createTestProduct
import ecommerce.product.data.ProductResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UpdateDeleteProductAPITest {
    @Test
    fun `updateProduct() should be able to update product, and return 'ok 200' response`() {
        createTestProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .body(FLAT_WHITE)
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/1")
                .then().log().all().extract()

        val product = response.body().`as`(ProductResponse::class.java)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertProductEquals(product, FLAT_WHITE, 1)
    }

    @Test
    fun `updateProduct() should return 'not found 404' response, when failed to update product`() {
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
    fun `deleteProductById() should be able to delete product, and return '204' response`() {
        createTestProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `deleteProductById() should return 'not found 404' response, when list of products is empty`() {
        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `deleteProductById() should return 'not found 404' response, when product id not found`() {
        createTestProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/api/products/100")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
