package ecommerce.api.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {
    @LocalServerPort
    var port: Int = 0

    private lateinit var jwtToken: String

    @BeforeAll
    fun loginAndGetToken() {
        RestAssured.port = port

        val loginResponse =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(
                    """
                    {
                        "email": "admin@gmail.com",
                        "password": "admin1234"
                    }
                    """.trimIndent(),
                )
                .post("/api/members/login")
                .then()
                .extract()

        jwtToken = loginResponse.jsonPath().getString("accessToken")
    }

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `POST - create product`() {
        val json =
            """
            {
                "name": "Spiderman",
                "price": 1000.0,
                "imageUrl": "https://image.com/iron"
            }
            """.trimIndent()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .body(json)
            .post("/admin/api/products")
            .then()
            .statusCode(201)
    }

    @Test
    fun `GET - list products`() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .get("/admin/api/products")
            .then()
            .statusCode(200)
            .body("size()", Matchers.greaterThanOrEqualTo(0))
    }

    @Test
    fun `GET - fetch existing product`() {
        val id = createProductAndGetId()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .get("/admin/api/products/$id")
            .then()
            .statusCode(200)
            .body("name", Matchers.startsWith("Test-"))
    }

    @Test
    fun `PATCH - update existing product`() {
        val id = createProductAndGetId()

        val updated =
            """
            {
                "name": "Updated Name",
                "price": 2000.0,
                "imageUrl": "https://image.com/updated"
            }
            """.trimIndent()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .body(updated)
            .patch("/admin/api/products/$id")
            .then()
            .statusCode(200)
    }

    @Test
    fun `DELETE - remove product`() {
        val id = createProductAndGetId()

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .delete("/admin/api/products/$id")
            .then()
            .statusCode(204)
    }

    private fun createProductAndGetId(): Int {
        val uniqueSuffix = UUID.randomUUID().toString().take(8)
        val uniqueName = "Test-$uniqueSuffix"

        val json =
            """
            {
                "name": "$uniqueName",
                "price": 10.0,
                "imageUrl": "https://img.com/test"
            }
            """.trimIndent()

        val location =
            RestAssured.given()
                .header("Authorization", "Bearer $jwtToken")
                .contentType(ContentType.JSON)
                .body(json)
                .post("/admin/api/products")
                .then()
                .statusCode(201)
                .extract()
                .header("Location")

        return location.substringAfterLast("/").toInt()
    }
}
