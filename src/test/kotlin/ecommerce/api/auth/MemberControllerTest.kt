package ecommerce.api.auth

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.springframework.test.annotation.DirtiesContext


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberControllerTest {

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `POST - register user`() {
        val json = """
            {
                "email": "test@example.com",
                "password": "secure123",
                "name": "Test User"
            }
        """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/members/register")
            .then()
            .statusCode(201)
            .body("accessToken", Matchers.notNullValue())
    }

    @Test
    fun `POST - login user`() {
        registerTestUser()

        val json = """
            {
                "email": "test@example.com",
                "password": "secure123"
            }
        """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/members/login")
            .then()
            .statusCode(200)
            .body("accessToken", Matchers.notNullValue())
    }

    @Test
    fun `GET - get member info with token`() {
        registerTestUser()
        val token = loginAndGetToken()

        RestAssured
            .given()
            .header("Authorization", "Bearer $token")
            .get("/api/members/me/token")
            .then()
            .statusCode(200)
            .body("email", Matchers.equalTo("test@example.com"))
    }

    private fun registerTestUser() {
        val json = """
            {
                "email": "test@example.com",
                "password": "secure123",
                "name": "Test User"
            }
        """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/members/register")
            .then()
            .statusCode(201)
    }

    private fun loginAndGetToken(): String {
        val json = """
            {
                "email": "test@example.com",
                "password": "secure123"
            }
        """.trimIndent()

        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post("/api/members/login")
            .then()
            .statusCode(200)
            .extract()
            .path("accessToken")
    }
}
