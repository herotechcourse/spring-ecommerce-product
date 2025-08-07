package ecommerce.api.controller

import ecommerce.dto.ProductRequestDTO
import ecommerce.service.ProductService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartControllerTest {
    @Autowired
    private lateinit var productService: ProductService

    @LocalServerPort
    var port: Int = 0

    private lateinit var jwtToken: String

    @BeforeAll
    fun loginAndCreateProduct() {
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

        productService.insert(
            ProductRequestDTO(
                name = "test",
                price = 5.0,
                imageUrl = "http://localhost:8080/",
            ),
        )
    }

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `GET - fetch my cart`() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.JSON)
            .get("/api/cart/mine")
            .then()
            .statusCode(200)
            .body("items", notNullValue())
    }

    @Test
    fun `POST - add item to cart`() {
        val quantity = 2

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.URLENC)
            .param("productId", 1)
            .param("quantity", quantity)
            .post("/api/cart/items")
            .then()
            .statusCode(200)
            .body("items.find { it.productId == 1 }.quantity", equalTo(quantity))
    }

    @Test
    fun `DELETE - remove an item from cart`() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .contentType(ContentType.URLENC)
            .param("productId", 1)
            .param("quantity", 1)
            .post("/api/cart/items")
            .then()
            .statusCode(200)

        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .delete("/api/cart/items/1")
            .then()
            .statusCode(204)
    }

    @Test
    fun `DELETE - clear cart`() {
        RestAssured.given()
            .header("Authorization", "Bearer $jwtToken")
            .delete("/api/cart/clear")
            .then()
            .statusCode(204)
    }
}
