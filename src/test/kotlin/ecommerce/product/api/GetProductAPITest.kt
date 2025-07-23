package ecommerce.product.api

import ecommerce.CustomAssertExtension.shouldEquals
import ecommerce.TestFixture.ValidRequest.AMERICANO
import ecommerce.TestFixture.ValidRequest.FLAT_WHITE
import ecommerce.TestFixture.createTestProduct
import ecommerce.product.data.ProductResponse
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GetProductAPITest {
    @Test
    fun `getProducts() should be able to read a product and return 'ok 200' response`() {
        createTestProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        val products = response.body().`as`(object : TypeRef<List<ProductResponse>>() {})

        assertThat(products).isNotEmpty()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        products[0].shouldEquals(AMERICANO, 1)
    }

    @Test
    fun `getProducts() should be able to read products, and return 'ok 200' response`() {
        createTestProduct(AMERICANO)
        createTestProduct(FLAT_WHITE)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        val products = response.body().`as`(object : TypeRef<List<ProductResponse>>() {})

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", ProductResponse::class.java)).hasSize(2)

        products[0].shouldEquals(AMERICANO, 1)
        products[1].shouldEquals(FLAT_WHITE, 2)
    }

    @Test
    fun `getProducts() should return 'ok 200' response`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        val products = response.body().`as`(object : TypeRef<List<ProductResponse>>() {})

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(products).isEmpty()
    }
}
