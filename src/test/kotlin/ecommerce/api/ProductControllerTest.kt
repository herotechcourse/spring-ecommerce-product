package ecommerce.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {
    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `POST - create product`() {
        val json =
            """
            {
                "name": "Iron Man",
                "price": 1000.0,
                "imageUrl": "https://image.com/iron"
            }
            """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/products")
            .then()
            .statusCode(201)
    }

    @Test
    fun `GET - list products`() {
        RestAssured
            .given()
            .get("/api/products")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
    }

    @Test
    fun `GET - fetch existing product`() {
        val id = createProductAndGetId()

        RestAssured
            .given()
            .get("/api/products/$id")
            .then()
            .statusCode(200)
            .body("name", equalTo("Test"))
    }

    @Test
    fun `PUT - update existing product`() {
        val id = createProductAndGetId()

        val updated =
            """
            {
                "name": "Updated Name",
                "price": 2000.0,
                "imageUrl": "https://image.com/updated"
            }
            """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(updated)
            .put("/api/products/$id")
            .then()
            .statusCode(200)
    }

    @Test
    fun `DELETE - remove product`() {
        val id = createProductAndGetId()

        RestAssured
            .given()
            .delete("/api/products/$id")
            .then()
            .statusCode(204)
    }

    private fun createProductAndGetId(): Int {
        val json =
            """
            {
                "name": "Test",
                "price": 10.0,
                "imageUrl": "https://img.com/test"
            }
            """.trimIndent()

        val location =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .header("Location")

        return location.substringAfterLast("/").toInt()
    }
}
