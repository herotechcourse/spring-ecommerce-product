package ecommerce.controller.member

import ecommerce.dto.auth.LoginRequest
import ecommerce.dto.user.UserRequestDTO
import ecommerce.service.MemberAuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberAuthControllerTest {
    @Autowired
    private lateinit var memberAuthService: MemberAuthService

    @Test
    fun `signIn User`() {
        val user =
            UserRequestDTO(
                name = "test",
                email = "temp@temp.com",
                password = "test-456",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(user)
                .contentType(ContentType.JSON)
                .`when`().post("api/member/auth/signUp")
                .then().log().all().extract()
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(response.header("Authorization")).isNotNull
    }

    @Test
    fun signUp() {
        val user =
            UserRequestDTO(
                name = "test",
                email = "temp2@temp.com",
                password = "test-456",
            )
        memberAuthService.signUp(user)
        val loginRequest =
            LoginRequest(
                email = "temp2@temp.com",
                password = "test-456",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .`when`().post("api/member/auth/signIn")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.header("Authorization")).isNotEmpty
    }
}
