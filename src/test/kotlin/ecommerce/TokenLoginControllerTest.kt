package ecommerce

import ecommerce.dto.TokenRequest
import ecommerce.repository.ProductRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TokenLoginControllerTest {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    companion object {
        @JvmStatic
        fun invalidRegisterRequests(): List<TokenRequest> =
            listOf(
                // invalid mail
                TokenRequest("@", "abcdef1234"),
                // invalid password
                TokenRequest("a@mail.com", "abcd"),
                // invalid mail
                TokenRequest("", "abcdef1234"),
                // invalid password
                TokenRequest("a@mail.com", ""),
                // already existent member
                TokenRequest("sandra@email.com", "MyPassword"),
            )
    }

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE IF EXISTS cart_items")
        jdbcTemplate.execute("DROP TABLE IF EXISTS carts")
        jdbcTemplate.execute("DROP TABLE members IF EXISTS")
        jdbcTemplate.execute(
            "CREATE TABLE members(" + " id SERIAL, email VARCHAR(20) UNIQUE, password VARCHAR(50), role VARCHAR(10))",
        )

        val splitUpAttributes: List<Array<String>> =
            listOf(
                "sandra@email.com MyPassword user",
                "simon@email.com Hello1234 user",
                "sara@email.com 1234567! user",
                "sam@email.com abcdefghijkl user",
            ).map { name -> name.split(" ").toTypedArray() }.toList()
        jdbcTemplate.batchUpdate("INSERT INTO members(email, password, role) VALUES (?,?,?)", splitUpAttributes)
    }

    @Test
    fun `test registering valid member`() {
        val request = TokenRequest(email = "newmember@gmail.com", password = "abcdef1234")
        val response =
            RestAssured.given().log().all().body(request).contentType(ContentType.JSON).`when`()
                .post("/api/members/register").then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterRequests")
    fun `test registering invalid members`(request: TokenRequest) {
        val response =
            RestAssured.given().log().all().body(request).contentType(ContentType.JSON).`when`()
                .post("/api/members/register").then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `test valid logins`() {
        val request = TokenRequest(email = "simon@email.com", password = "Hello1234")
        val response =
            RestAssured.given().log().all().body(request).contentType(ContentType.JSON).`when`()
                .post("/api/members/login").then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `test login with non-registered member`() {
        val request = TokenRequest(email = "member@email.com", password = "MyPassword#")
        val response =
            RestAssured.given().log().all().body(request).contentType(ContentType.JSON).`when`()
                .post("/api/members/login").then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        assertThat(response.body().asString()).contains("No account with email exists")
    }

    @Test
    fun `test login with incorrect password`() {
        val request = TokenRequest(email = "sam@email.com", password = "MyPassword#")
        val response =
            RestAssured.given().log().all().body(request).contentType(ContentType.JSON).`when`()
                .post("/api/members/login").then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `test request with valid token`() {
        val loginRequest = TokenRequest(email = "sam@email.com", password = "abcdefghijkl")
        val loginResponse =
            RestAssured.given().log().all()
                .body(loginRequest).contentType(ContentType.JSON)
                .`when`()
                .post("/api/members/login")
                .then().log().all()
                .extract()

        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value())

        val token = loginResponse.body().jsonPath().getString("token")
        val tokenResponse =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $token")
                .`when`()
                .get("/api/members/me/token")
                .then().log().all()
                .extract()

        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `test request with invalid token`() {
        val token = "ndwndwoljdwpfkwkdsq.DNlwfk3wld'wamclwfjkepojfo3jf"
        val tokenResponse =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $token")
                .`when`()
                .get("/api/members/me/token")
                .then().log().all()
                .extract()

        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `test request without 'Authorization' header`() {
        val loginRequest = TokenRequest(email = "sam@email.com", password = "abcdefghijkl")
        val loginResponse =
            RestAssured.given().log().all()
                .body(loginRequest).contentType(ContentType.JSON)
                .`when`()
                .post("/api/members/login")
                .then().log().all()
                .extract()

        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value())

        val token = loginResponse.body().jsonPath().getString("token")
        val tokenResponse =
            RestAssured.given().log().all()
                .header("Location", "Bearer $token")
                .`when`()
                .get("/api/members/me/token")
                .then().log().all()
                .extract()

        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
