package ecommerce

import ecommerce.member.domain.Member
import ecommerce.member.dto.TokenRequest
import ecommerce.member.repository.MemberRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DROP TABLE CART_ITEMS IF EXISTS")
        jdbcTemplate.execute("DROP TABLE MEMBERS IF EXISTS")
        jdbcTemplate.execute(
            """
            CREATE TABLE MEMBERS (
                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                role VARCHAR(50) NOT NULL
            )
            """,
        )
        memberRepository.insert(Member(email = "example@email.com", password = "password123qwerty", role = "USER"))
    }

    @Test
    fun `register with valid credentials should return 201 and token`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "newuser@email.com", password = "password123qwerty"))
                .`when`()
                .post("/api/members/register")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.jsonPath().getString("accessToken")).isNotEmpty()
    }

    @Test
    fun `register with invalid email format should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "invalid-email", password = "password123qwerty"))
                .`when`()
                .post("/api/members/register")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("details.email")).isEqualTo("Email must be a valid email address")
    }

    @Test
    fun `register with blank email should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "", password = "password123qwerty"))
                .`when`()
                .post("/api/members/register")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("details.email")).isEqualTo("Email cannot be blank")
    }

    @Test
    fun `register with short password should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "newuser@email.com", password = "short"))
                .`when`()
                .post("/api/members/register")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("details.password")).isEqualTo("Password must be at least 8 characters long")
    }

    @Test
    fun `register with existing email should return 409`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "example@email.com", password = "password123qwerty"))
                .`when`()
                .post("/api/members/register")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Email is already registered")
    }

    @Test
    fun `login with valid credentials should return 200 and token`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "example@email.com", password = "password123qwerty"))
                .`when`()
                .post("/api/members/login")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getString("accessToken")).isNotEmpty()
    }

    @Test
    fun `login with invalid email should return 403`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "nonexistent@email.com", password = "password123qwerty"))
                .`when`()
                .post("/api/members/login")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Member not found with email: nonexistent@email.com")
    }

    @Test
    fun `login with invalid password should return 403`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "example@email.com", password = "wrongpassword"))
                .`when`()
                .post("/api/members/login")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Invalid password for email: example@email.com")
    }

    @Test
    fun `get member info with valid token should return 200 and member details`() {
        val tokenResponse =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TokenRequest(email = "example@email.com", password = "password123qwerty"))
                .`when`()
                .post("/api/members/login")
                .then()
                .extract()
                .jsonPath().getString("accessToken")

        val response =
            RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer $tokenResponse")
                .`when`()
                .get("/api/members/me")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getLong("id")).isEqualTo(1L)
        assertThat(response.jsonPath().getString("email")).isEqualTo("example@email.com")
    }

    @Test
    fun `get member info with invalid token should return 401`() {
        val response =
            RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer invalid-token")
                .`when`()
                .get("/api/members/me")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Unauthorized: Invalid or expired JWT token")
    }

    @Test
    fun `get member info without token should return 401`() {
        val response =
            RestAssured.given()
                .log().all()
                .`when`()
                .get("/api/members/me")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Unauthorized: Missing Authorization header")
    }
}
