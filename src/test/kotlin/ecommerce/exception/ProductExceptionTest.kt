package ecommerce.exception

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductExceptionTest {
    @LocalServerPort
    private var port = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    fun `invalid name returns 400(too long)`() {
        val invalidRequestJson =
            """
            {
                "name": "ThisProductNameIsTooLongToBeValid",
                "price": 10.0,
                "img": "http://valid_image_url.com",
                "quantity": 10
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(invalidRequestJson).post("/products").then()
            .statusCode(400).body(
                "errors.name",
                equalTo(
                    "Name must be 1–15 characters and only include letters, digits, spaces, " +
                        "and allowed special characters:( ), [ ], +, -, &, /, _",
                ),
            )
    }

    @Test
    fun `invalid name returns 400(contains invalid character)`() {
        val invalidRequestJson =
            """
            {
            "name" : "%ProductName%",
            "price": 10.0,
            "img": "http://valid_image_url.com",
            "quantity": 10
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(invalidRequestJson).post("/products").then()
            .statusCode(400).body(
                "errors.name",
                equalTo(
                    "Name must be 1–15 characters and only include letters, digits, spaces, and allowed " +
                        "special characters:( ), [ ], +, -, &, /, _",
                ),
            )
    }

    @Test
    fun `invalid price returns 400(less than 0)`() {
        val invalidRequestJson =
            """
            {
            "name" : "ProductName",
            "price": -10.0,
            "img": "http://valid_image_url.com",
            "quantity": 10
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(invalidRequestJson).post("/products").then()
            .statusCode(400).body("errors.price", equalTo("Price must be greater than 0"))
    }

    @Test
    fun `invalid image url returns 400(does not start with http or https)`() {
        val invalidRequestJson =
            """
            {
            "name" : "ProductName",
            "price": -10.0,
            "img": "invalid_image_url.com",
            "quantity": 10
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(invalidRequestJson).post("/products").then()
            .statusCode(400).body("errors.img", equalTo("url must begin with http:// or https:// and be a valid URL"))
    }

    @Test
    fun `duplicate product name returns 409 conflict`() {
        val initialProductName =
            """
            {
            "name" : "ProductName",
            "price": 10.0,
            "img": "http://valid_image_url.com",
            "quantity": 10
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(initialProductName).post("/products").then()
            .statusCode(201)

        val duplicateProduct =
            """
            {
            "name" : "ProductName",
            "price": 100.0,
            "img": "http://valid_image_url.com",
            "quantity": 100
            }
            }
            """.trimIndent()

        RestAssured.given().contentType(ContentType.JSON).body(duplicateProduct).post("/products").then()
            .statusCode(409).body("error", equalTo("Product name ProductName already exists."))
    }

    @Test
    fun `non existent product returns 404`() {
        val nonExistedId = 988L
        RestAssured.given().get(
            "/products/$nonExistedId",
        ).then().statusCode(404).body("error", equalTo("Product with ID $nonExistedId not found"))
    }

    @Test
    fun `delete non existent product returns 404`() {
        val nonExistedId = 988L
        RestAssured.given().delete(
            "/products/$nonExistedId",
        ).then().statusCode(404).body("error", equalTo("Product with ID $nonExistedId not found"))
    }
}
