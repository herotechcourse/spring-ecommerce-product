package ecommerce.controller

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.util.AssertionErrors.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun `register with valid data should return 201 Created`() {
        val registerRequest =
            RegisterRequest("valid@example.com", "SecureP@ss1", "name")
        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .`when`().post("/api/members/register")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()

        val token = response.jsonPath().getString("token")
        assertNotNull(token, "Token should not be null")
        assert(token.isNotBlank()) { "Token should not be blank" }
    }

    @Test
    fun `register with existing email should return 409 Conflict`() {
        val email = "repeat@example.com"
        val registerRequest = RegisterRequest(email, "SecureP@ss1", "name")
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(registerRequest)
            .`when`().post("/api/members/register")
            .then().statusCode(HttpStatus.CREATED.value())

        val duplicateRegisterRequest = RegisterRequest(email, "AnotherP@ss1", "name")
        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicateRegisterRequest)
                .`when`().post("/api/members/register")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value())
        assertThat(response.jsonPath().getString("error")).contains("Member with email $email already exists")
    }

    @Test
    fun `register with invalid email format should return 400 Bad Request`() {
        val invalidEmailRequest = RegisterRequest("invalid-email", "Password123!", "name")
        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidEmailRequest)
                .`when`().post("/api/members/register")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("errors.email")).contains("Email should be valid")
    }

    @Test
    fun `register with weak password should return 400 Bad Request`() {
        val weakPasswordRequest = RegisterRequest("valid@example.com", "weakpassword", "name")
        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(weakPasswordRequest)
                .`when`().post("/api/members/register")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(
            response.jsonPath().getString("errors.password"),
        ).contains("Password must contain at least one digit, one lowercase, one uppercase, and one special character")
    }

    @Test
    fun `login with valid credentials should return 200 OK`() {
        val email = "GoodEmail@example.com"
        val password = "LoginP@ss1"
        val name = "validName"
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(RegisterRequest(email, password, name))
            .`when`().post("/api/members/register")
            .then().statusCode(HttpStatus.CREATED.value())

        val loginRequest = LoginRequest(email, password)
        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        val token = response.jsonPath().getString("token")
        assertNotNull(token, "Token should not be null")
        assert(token.isNotBlank()) { "Token should not be blank" }
    }

    @Test
    fun `login with incorrect credentials should return 401 Unauthorized`() {
        val loginRequest = LoginRequest("nonexistent@example.com", "wrongpassword")

        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.jsonPath().getString("error")).contains("Invalid email or password.")
    }

    @Test
    fun `login with blank email should return 400 Bad Request`() {
        val blankEmailRequest = LoginRequest("", "password123")
        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(blankEmailRequest)
                .`when`().post("/api/members/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("errors.email")).contains("Email cannot be empty")
    }
}
