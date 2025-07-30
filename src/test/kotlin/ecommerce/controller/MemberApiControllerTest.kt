package ecommerce.controller

import ecommerce.dto.member.AuthResponse
import ecommerce.dto.member.MemberLoginRequest
import ecommerce.dto.member.MemberRegisterRequest
import ecommerce.repository.MemberRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Member Api Controller Integration Tests")
class MemberApiControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private fun registerApi(request: MemberRegisterRequest): ValidatableResponse {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`().post("/api/members/register")
            .then().log().all()
    }

    private fun loginApi(request: MemberLoginRequest): ValidatableResponse {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`().post("/api/members/login")
            .then().log().all()
    }

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        jdbcTemplate.execute("delete from cart_events")
        jdbcTemplate.execute("delete from cart_items")
        jdbcTemplate.execute("delete from carts")
        jdbcTemplate.execute("delete from products")
        jdbcTemplate.execute("delete from members")
    }

    @DisplayName("Integration Tests for User Registration (POST /api/members/register)")
    @Test
    fun `Should register a new member and return 201 Created`() {
        val email = "bo-${System.nanoTime()}@gmail.com"
        val request = MemberRegisterRequest("bo", email, "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.CREATED.value())

        assertThat(memberRepository.findByEmail(email)).isNotNull()
    }

    @Test
    fun `Should return 400 for blank username`() {
        val request = MemberRegisterRequest("", "noemail@gmail.com", "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 for blank email`() {
        val request = MemberRegisterRequest("bo", "", "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 for invalid email format`() {
        val request = MemberRegisterRequest("bo", "notanemail", "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 for blank password`() {
        val request = MemberRegisterRequest("bo", "notanemail", "")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 for password shorter than 8 characters`() {
        val request = MemberRegisterRequest("bo", "notanemail", "pass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 for duplicate email registration`() {
        val email = "bo@gmail.com"
        val request = MemberRegisterRequest("bo", email, "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.CREATED.value())

        registerApi(request)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @DisplayName("Integration Tests for User Login (POST /api/members/login)")
    @Test
    fun `Should log in an existing member and return 200 OK with token`() {
        val email = "bo-${System.nanoTime()}@gmail.com"
        val request = MemberRegisterRequest("bo", email, "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.CREATED.value())

        val loginRequest = MemberLoginRequest(email, "VerySafePass")
        loginApi(loginRequest)
            .assertThat().statusCode(HttpStatus.OK.value())
            .extract().`as`(AuthResponse::class.java).token.isNotBlank()
    }

    @Test
    fun `Should return 401 for non-existent email`() {
        val loginRequest = MemberLoginRequest("doesntexist@gmail.com", "verysafepass")
        loginApi(loginRequest)
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 401 for incorrect password`() {
        val request = MemberRegisterRequest("bo", "bo@gmail.com", "VerySafePass")
        registerApi(request)
            .assertThat().statusCode(HttpStatus.CREATED.value())

        val loginRequest = MemberLoginRequest("bo@gmail.com", "VerySafePassport")
        loginApi(loginRequest)
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 400 for blank email for login`() {
        val loginRequest = MemberLoginRequest("", "VerySafePassport")
        loginApi(loginRequest)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 for blank password for login`() {
        val loginRequest = MemberLoginRequest("bo@gmail.com", "")
        loginApi(loginRequest)
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }
}
