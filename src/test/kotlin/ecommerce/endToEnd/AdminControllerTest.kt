package ecommerce.endToEnd

import ecommerce.dto.ProductRequest
import ecommerce.dto.RegistrationRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {
    lateinit var token: String

    @BeforeEach
    fun loginAndGetToken() {
        val loginRequest =
            RegistrationRequest(
                "admin@test.com",
                "12345678",
            )

        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/members/login")
                .then().extract()

        token = response.body().jsonPath().getString("token")
    }

    @Test
    fun getProducts() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val names = response.body().jsonPath().getList<String>("name")
        assertThat(names).isNotEmpty()
        assertThat(names.size).isEqualTo(10)
    }

    @Test
    fun createAndGetProductById() {
        val productPayload =
            ProductRequest(
                name = "TestProduct",
                price = 19.99,
                imageUrl = "https://example.com/image.jpg",
            )

        // Create product
        RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(ContentType.JSON)
            .body(productPayload)
            .post("/api/admin/products")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())

        // Get all and extract ID
        val products =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/admin/products")
                .then().extract().jsonPath().getList("", Map::class.java)

        val createdProductId = products.find { it["name"] == "TestProduct" }?.get("id") as Int

        // Get by ID
        val response =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/admin/products/$createdProductId")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath()

        assertThat(response.getString("name")).isEqualTo("TestProduct")
    }

    @Test
    fun updateProduct() {
        val productPayload = ProductRequest("Original", 10.0, "https://example.com/image.jpg")

        // Create
        RestAssured.given()
            .auth().oauth2(token)
            .contentType(ContentType.JSON)
            .body(productPayload)
            .post("/api/admin/products")
            .then().statusCode(HttpStatus.CREATED.value())

        val id =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/admin/products")
                .then().extract().jsonPath().getList<Map<String, Any>>("")
                .first { it["name"] == "Original" }["id"]

        // Update
        val updated = ProductRequest("Updated", 12.5, "https://example.com/updated.jpg")
        RestAssured.given()
            .auth().oauth2(token)
            .contentType(ContentType.JSON)
            .body(updated)
            .put("/api/admin/products/$id")
            .then().statusCode(HttpStatus.NO_CONTENT.value())

        // Verify
        val response =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/admin/products/$id")
                .then().extract().jsonPath()

        assertThat(response.getString("name")).isEqualTo("Updated")
        assertThat(response.getFloat("price")).isEqualTo(12.5f)
    }

    @Test
    fun deleteProduct() {
        val productPayload = ProductRequest("ToDelete", 5.0, "https://example.com/delete.jpg")

        // Create
        RestAssured.given()
            .auth().oauth2(token)
            .contentType(ContentType.JSON)
            .body(productPayload)
            .post("/api/admin/products")
            .then().statusCode(HttpStatus.CREATED.value())

        val id =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/admin/products")
                .then().extract().jsonPath().getList<Map<String, Any>>("")
                .first { it["name"] == "ToDelete" }["id"]

        // Delete
        RestAssured.given()
            .auth().oauth2(token)
            .delete("/api/admin/products/$id")
            .then().statusCode(HttpStatus.NO_CONTENT.value())
    }
}
