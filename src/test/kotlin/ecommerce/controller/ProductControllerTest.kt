package ecommerce.controller

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
        val productJson = mapOf(
            "name" to "cafe",
            "price" to 39.0,
            "imageUrl" to "https://test.com/image.jpg"
        )

        val response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(productJson)
            .`when`().post("/api/products")
            .then().extract().response()

        productId = response.jsonPath().getLong("id")
    }

    @Test
    fun addProduct() {
        val productJson = mapOf(
            "name" to "table",
            "price" to 45.0,
            "imageUrl" to "https://test.com/image2.jpg"
        )

        val response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(productJson)
            .`when`().post("/api/products")
            .then().extract().response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val created = response.jsonPath()
        assertThat(created.getString("name")).isEqualTo("table")
        assertThat(created.getDouble("price")).isEqualTo(45.0)
        assertThat(created.getString("imageUrl")).isEqualTo("https://test.com/image2.jpg")
    }

    @Test
    fun getProducts_returnsList() {
        val response = RestAssured
            .given().log().all()
            .`when`().get("/api/products")
            .then().extract().response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val productList = response.jsonPath().getList("", Map::class.java)
        assertThat(productList).isNotEmpty
    }
}



