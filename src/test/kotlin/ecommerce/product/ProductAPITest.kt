package ecommerce.product

import ecommerce.TextFixture.AMERICANO
import ecommerce.TextFixture.FLAT_WHITE
import ecommerce.TextFixture.createProduct
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductAPITest {
    @Test
    fun `createProduct() should be able return 'created 201' response`() {
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
    fun `getProducts() should be able to read a product and return 'ok 200' response`() {
        createProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", ProductResponse::class.java)).hasSize(1)
    }

    @Test
    fun `getProducts() should be able to read products, and return 'ok 200' response`() {
        createProduct(AMERICANO)
        createProduct(FLAT_WHITE)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", ProductResponse::class.java)).hasSize(2)
    }

    @Test
    fun `getProducts() should return 'ok 200' response`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `updateProduct() should be able to update product, and return 'ok 200' response`() {
        createProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .body(FLAT_WHITE)
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
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
        createProduct(AMERICANO)

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
        createProduct(AMERICANO)

        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/api/products/100")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
