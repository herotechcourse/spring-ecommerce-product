package ecommerce.controller

import ecommerce.auth.JwtTokenProvider
import ecommerce.controller.api.AuthController
import ecommerce.dao.JdbcMemberDAO
import ecommerce.dto.AuthResponse
import ecommerce.dto.LoginForm
import ecommerce.dto.MemberResponse
import ecommerce.dto.RegisterForm
import ecommerce.model.Member
import ecommerce.service.AuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired private lateinit var jdbcMemberDAO: JdbcMemberDAO

    @Autowired private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired private lateinit var authService: AuthService

    @Autowired private lateinit var controller: AuthController

    @BeforeEach
    fun setUp() {
        jdbcMemberDAO = JdbcMemberDAO(jdbcTemplate)
        authService = AuthService(jdbcMemberDAO, jwtTokenProvider)

        jdbcTemplate.execute("DROP TABLE member CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE member
            (
                id       LONG         NOT NULL AUTO_INCREMENT,
                email    VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role     VARCHAR(255) DEFAULT NULL,
                PRIMARY KEY (id)
            );""",
        )

        val query =
            """INSERT INTO member (email, password, role) VALUES ( 'san@htc.com', 'san1234', 'admin');
            INSERT INTO member (email, password, role) VALUES ( 'dan@htc.com', 'dan1234', 'admin');
            INSERT INTO member (email, password) VALUES ( 'ann@htc.com', 'ann1234');
            INSERT INTO member (email, password) VALUES ( 'min@htc.com', 'min1234');"""
        jdbcTemplate.batchUpdate(query)

        controller = AuthController(authService)
    }

    @Test
    fun registerMember() {
        val email = "test@email.com"
        val password = "test1234"
        val testForm = RegisterForm(email, password)
        val response = controller.registerMember(testForm)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isInstanceOf(AuthResponse::class.java)
    }

    @Test
    fun `registerMember() - should insert member and return 201 when form is valid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "test@email.com", password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.body().jsonPath().getString("accessToken")).isInstanceOf(String::class.java)
    }

    @Test
    fun `registerMember() - should return 400 when email is blank`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "", password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()

        val targets =
            listOf(
                "Email is required",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["email"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `registerMember() - should return 400 when email is not valid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "abc.abc.com", password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()

        val targets =
            listOf(
                "Email must be valid",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["email"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `registerMember() - should return 400 when password is shorter than 4 characters long`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "test@email.com", password = "123"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()

        val targets =
            listOf(
                "Password must be at least 4 characters long",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["password"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `registerMember() - should return 400 when email already exists`() {
        val targetEmail = "dan@htc.com"
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = targetEmail, password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()

        val targets =
            listOf(
                "Email $targetEmail already exists.",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["email"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun loginMember() {
        val email = "san@htc.com"
        val password = "san1234"
        val testForm = LoginForm(email, password)
        val response = controller.loginMember(testForm)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isInstanceOf(AuthResponse::class.java)
    }

    @Test
    fun `loginMember() - should login member and return 200 when form is valid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "san@htc.com", password = "san1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().jsonPath().getString("accessToken")).isInstanceOf(String::class.java)
    }

    @Test
    fun `loginMember() - should return 400 when email is blank`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "", password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()

        val targets =
            listOf(
                "Please enter your email address",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["email"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `loginMember() - should return 400 when password is blank`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "test@email.com", password = ""))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()

        val targets =
            listOf(
                "Please enter your password",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["password"]
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `loginMember() - should return 401 when email is invalid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "test@email.com", password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `loginMember() - should return 401 when password is invalid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Member(email = "dan@htc.com", password = "test1234"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun tokenLogin() {
        val targetEmail = "dan@htc.com"
        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(targetEmail, "dan1234"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        val member =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $accessToken")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().get("/api/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().`as`(MemberResponse::class.java)

        assertThat(member.email).isEqualTo(targetEmail)
    }
}
