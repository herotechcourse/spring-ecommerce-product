package ecommerce.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {
    @Test
    fun `login() be able return '200' response`() {
        val requestBody =
            mapOf(
                "email" to "admin@test.com",
                "password" to "1234",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .`when`().post("/api/auth/login")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `login() returns a '401' response if user is invalid`() {
        val requestBody =
            mapOf(
                "email" to "invalid@test.com",
                "password" to "1234",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .`when`().post("/api/auth/login")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
