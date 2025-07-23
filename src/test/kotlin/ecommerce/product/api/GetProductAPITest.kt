package ecommerce.product.api

import ecommerce.product.data.ProductResponse
import ecommerce.product.helper.TestFixture.ValidRequest.AMERICANO
import ecommerce.product.helper.TestFixture.ValidRequest.FLAT_WHITE
import ecommerce.product.helper.TestFixture.postTestProduct
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
        postTestProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        val products = response.body().`as`(object : TypeRef<List<ProductResponse>>() {})

        assertThat(products).isNotEmpty()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        assertThat(products[0].id).isEqualTo(1)
        assertThat(products[0].name).isEqualTo(AMERICANO.name)
        assertThat(products[0].price).isEqualTo(AMERICANO.price.toPlainString())
        assertThat(products[0].imageUrl).isEqualTo(AMERICANO.imageUrl)
    }

    @Test
    fun `getProducts() should be able to read products, and return 'ok 200' response`() {
        postTestProduct(AMERICANO)
        postTestProduct(FLAT_WHITE)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        val products = response.body().`as`(object : TypeRef<List<ProductResponse>>() {})

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", ProductResponse::class.java)).hasSize(2)

        assertThat(products[0].id).isEqualTo(1)
        assertThat(products[0].name).isEqualTo(AMERICANO.name)
        assertThat(products[0].price).isEqualTo(AMERICANO.price.toPlainString())
        assertThat(products[0].imageUrl).isEqualTo(AMERICANO.imageUrl)

        assertThat(products[1].id).isEqualTo(2)
        assertThat(products[1].name).isEqualTo(FLAT_WHITE.name)
        assertThat(products[1].price).isEqualTo(FLAT_WHITE.price.toPlainString())
        assertThat(products[1].imageUrl).isEqualTo(FLAT_WHITE.imageUrl)
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
