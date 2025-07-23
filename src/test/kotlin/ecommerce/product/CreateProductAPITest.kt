package ecommerce.product

import ecommerce.TextFixture
import ecommerce.TextFixture.AMERICANO
import ecommerce.TextFixture.AssertTamplate.assertProductEquals
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CreateProductAPITest {
    @Test
    fun `createProduct() should be able return 'created 201' response`() {
        val response =
            RestAssured
                .given().log().all()
                .body(AMERICANO)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val product =
            response.body().`as`(ProductResponse::class.java)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertProductEquals(product, AMERICANO, 1)
    }

    @Test
    fun `createProduct() should return 400 Bad Request when imageUrl exceeds max length`() {
        val response =
            RestAssured
                .given().log().all()
                .body(TextFixture.Dummy.SUPER_LONG_URL)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `createProduct() should return 400 Bad Request when name is missing`() {
        val response =
            RestAssured
                .given().log().all()
                .body(TextFixture.Dummy.NO_NAME)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `createProduct() should return 400 Bad Request when price is missing`() {
        val response =
            RestAssured
                .given().log().all()
                .body(TextFixture.Dummy.NO_PRICE)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `createProduct() should return 400 Bad Request when imageUrl is missing`() {
        val response =
            RestAssured
                .given().log().all()
                .body(TextFixture.Dummy.NO_URL)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }
}
