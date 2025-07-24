package ecommerce.api.product

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductValidationTest {
    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `should not create a product with invalid characters`() {
        val json =
            """
            {
                "name": "Iron Straße",
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
            .statusCode(400)
            .body("errors.name", Matchers.equalTo("Invalid characters"))
    }

    @Test
    fun `should not create a product with more than 15 characters`() {
        val json =
            """
            {
                "name": "Blue is the warmest color in the universe",
                "price": 1000.0,
                "imageUrl": "https://image.com/blue"
            }
            """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/products")
            .then()
            .statusCode(400)
            .body("errors.name", Matchers.equalTo("Name must have at maximum 15 characters"))
    }

    @Test
    fun `should not create a product without name`() {
        val json =
            """
            {
                "name": " ",
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
            .statusCode(400)
            .body("errors.name", Matchers.equalTo("Product name is required"))
    }

    @Test
    fun `should not create a product with negative price`() {
        val json =
            """
            {
                "name": "Iron Man",
                "price": -1000.0,
                "imageUrl": "https://image.com/iron"
            }
            """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/products")
            .then()
            .statusCode(400)
            .body("errors.price", Matchers.equalTo("Product price must be positive"))
    }

    @Test
    fun `should not create a product with invalid URL`() {
        val json =
            """
            {
                "name": "Wonder Woman",
                "price": 1000.0,
                "imageUrl": "www.image.com/wonder"
            }
            """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/products")
            .then()
            .statusCode(400)
            .body("errors.imageUrl", Matchers.equalTo("URL must start with http:// or https://"))
    }
}
