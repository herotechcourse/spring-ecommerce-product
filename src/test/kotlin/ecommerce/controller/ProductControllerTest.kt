package ecommerce.controller

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
    private var productId: Long = -1

    @BeforeEach
    fun createProducts() {
        val product = Product(0, "cafe", 39.0, "www.test")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().extract().response()

        productId = response.jsonPath().getLong("id")
    }

    @Test
    fun addProduct() {
        val product = Product(0, "table", 45.0, "test.com")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().extract().response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val created = response.`as`(Product::class.java)
        assertThat(created.name).isEqualTo("table")
        assertThat(created.price).isEqualTo(45.0)
        assertThat(created.imageUrl).isEqualTo("test.com")
    }

    @Test
    fun getProducts() {
        val response =
            RestAssured
                .given().log().all()
                .`when`().get("/api/products")
                .then().extract().response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val productList = response.jsonPath().getList("", Product::class.java)
        assertThat(productList).isNotEmpty
    }

    @Test
    fun getProduct() {
        val response =
            RestAssured
                .given().log().all()
                .`when`().get("/api/products/$productId")
                .then().extract().response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val found = response.`as`(Product::class.java)
        assertThat(found.name).isEqualTo("cafe")
        assertThat(found.price).isEqualTo(39.0)
    }
}
