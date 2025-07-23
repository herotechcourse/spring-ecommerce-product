package ecommerce.controller.guest

import ecommerce.dto.auth.TokenRequest
import ecommerce.dto.user.UserDTO
import ecommerce.enums.UserRole
import ecommerce.service.AuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {
    @Autowired
    private lateinit var authService: AuthService

    @Test
    fun `signIn User`() {
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
                .`when`().post("api/auth/signup")
                .then().log().all().extract()
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(response.header("Authorization")).isNotNull
    }

    @Test
    fun `signIn Admin`() {
        val user =
            UserDTO(
                name = "test",
                email = "admin@temp.com",
                password = "test-456",
                role = UserRole.ADMIN,
            )
        val response =
            RestAssured
                .given().log().all()
                .body(user)
                .contentType(ContentType.JSON)
                .`when`().post("api/auth/signup")
                .then().log().all().extract()
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(response.header("Authorization")).isNotNull
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
                .`when`().post("api/auth/signIn")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.header("Authorization")).isNotEmpty
    }
}