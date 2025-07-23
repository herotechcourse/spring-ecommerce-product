package ecommerce.controller

import ecommerce.dto.TokenRequest
import ecommerce.dto.UserDTO
import ecommerce.service.AuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {
    @Autowired
    private lateinit var authService: AuthService

    @Test
    fun signIn() {
        val user =
            UserDTO(
                name = "test",
                email = "temp@temp.com",
                password = "test-456",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(user)
                .contentType(ContentType.JSON)
                .`when`().post("/auth/signup")
                .then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED)
        assertThat(response.header("Authorization")).isNotEmpty
    }

    @Test
    fun signUp() {
        val user =
            UserDTO(
                name = "test",
                email = "temp2@temp.com",
                password = "test-456",
            )
        authService.signUp(user)
        val tokenRequest =
            TokenRequest(
                email = "temp2@temp.com",
                password = "test-456",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(ContentType.JSON)
                .`when`().post("/auth/signin")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK)
        assertThat(response.header("Authorization")).isNotEmpty
    }
}
