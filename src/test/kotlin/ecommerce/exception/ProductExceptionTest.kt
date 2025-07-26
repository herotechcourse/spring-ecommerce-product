package ecommerce.exception

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.dto.product.CreateProductRequest
import ecommerce.dto.product.ProductResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

private fun Int.isEqualTo(statusCode: Int) {}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductExceptionTest {
    @LocalServerPort
    private var port = 0
    private lateinit var authToken: String

    @BeforeEach
    fun setupAuthentication() {
        RestAssured.port = port
        RestAssured.baseURI = "http://localhost"
        val registerRequest = RegisterRequest("validEmail@email.com", "SecureP@ss1")
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
            .`when`().post("/api/members/register")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()

        val loginRequest = LoginRequest("validEmail@email.com", "SecureP@ss1")
        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        authToken = response.jsonPath().getString("token")
        assertThat(authToken).isNotNull().isNotBlank()
    }

    private fun createProductWithAuth(
        id: Long,
        name: String,
        price: Double,
        img: String,
        quantity: Int,
    ): ProductResponse {
        val createRequest = CreateProductRequest(name, price, img, quantity)
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(createRequest) // Send the DTO
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        val locationHeader = response.header("Location")
        assertThat(locationHeader).isNotNull()
        val createdProductId = locationHeader.substringAfterLast("/").toLong()

        return RestAssured.given()
            .header("Authorization", "Bearer $authToken")
            .contentType(ContentType.JSON)
            .`when`().get("/products/$createdProductId")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getObject("", ProductResponse::class.java)
    }

    @Test
    fun `invalid name returns 400(too long)`() {
        val createRequest =
            CreateProductRequest(
                name = "ThisProductNameIsTooLongToBeValid",
                price = 10.0,
                img = "https://valid_image_url.com/img.jpg",
                quantity = 10,
            )
        val response =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer $authToken")
                .body(createRequest)
                .`when`().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value()).extract()

        val errorMessage = response.jsonPath().getString("errors.name")
        assertThat(errorMessage).contains("Name must be 1–15 characters")
    }

    @Test
    fun `invalid name returns 400(contains invalid character)`() {
        val invalidProductName = "%ProductName%"
        val createRequest = CreateProductRequest(invalidProductName, 10.0, "https://valid_image_url.com/img.jpg", 10)
        val response =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer $authToken")
                .body(createRequest)
                .post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value()).extract()
        val errorMessage = response.jsonPath().getString("errors.name")
        assertThat(errorMessage).contains("Name must be 1–15 characters")
    }

    @Test
    fun `invalid price returns 400(less than 0)`() {
        val createRequest =
            CreateProductRequest(
                name = "ValidName",
                price = 0.0,
                img = "https://valid_image_url.com/img.jpg",
                quantity = 10,
            )
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(createRequest)
                .`when`().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
        val errorMessage = response.jsonPath().getString("errors.price")
        assertThat(errorMessage).contains("Price must be greater than 0")
    }

    @Test
    fun `invalid image url returns 400(does not start with http or https)`() {
        val createRequest =
            CreateProductRequest(
                name = "ValidName",
                price = 0.0,
                img = "invalidImage.jpg",
                quantity = 10,
            )
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .body(createRequest)
                .`when`().post("/products")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
        val errorMessage = response.jsonPath().getString("errors.img")
        assertThat(errorMessage).contains("url must begin with http:// or https:// and be a valid URL")
    }

    @Test
    fun `duplicate product name`() {
        val productName = "ProductName"
        createProductWithAuth(10, productName, 10.0, "https://valid_image_url.com/img1.jpg", 10)
        val duplicateProductRequest =
            CreateProductRequest(
                name = "ProductName",
                price = 10.0,
                img = "https://valid_image_url.com/img1.jpg",
                quantity = 10,
            )
        val response =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(duplicateProductRequest)
                .header("Authorization", "Bearer $authToken")
                .post("/products")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value()).extract()

        val errorMessage = response.jsonPath().getString("error")
        assertThat(HttpStatus.CONFLICT.value().isEqualTo(response.statusCode()))
        assertThat(errorMessage).contains("Product name ProductName already exists.")
    }

    @Test
    fun `non existent product returns 404`() {
        val nonExistedId = 988L
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .`when`().get("/products/$nonExistedId")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
        val errorMessage = response.jsonPath().getString("error")
        assertThat(HttpStatus.NOT_FOUND.value().isEqualTo(response.statusCode()))
        assertThat(errorMessage).contains("Product with ID $nonExistedId not found")
    }

    @Test
    fun `delete non existent product returns 404`() {
        val nonExistedId = 988L
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $authToken")
                .`when`().delete("/products/$nonExistedId")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
        val errorMessage = response.jsonPath().getString("error")
        assertThat(HttpStatus.NOT_FOUND.value().isEqualTo(response.statusCode()))
        assertThat(errorMessage).contains("Product with ID $nonExistedId not found")
    }
}
