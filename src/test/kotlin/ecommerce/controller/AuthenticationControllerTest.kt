package ecommerce.controller

import ecommerce.configuration.JwtTokenProvider
import ecommerce.dto.LoginRequest
import ecommerce.dto.RegistrationRequest
import ecommerce.repository.MemberRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthenticationControllerTest {
    lateinit var token: String

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    fun registerMember() {
        val registrationRequest =
            RegistrationRequest(
                "test",
                "test@test.com",
                "12345678",
            )

        val token =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(registrationRequest)
                .post("/api/members/register")
                .then().extract().body().jsonPath().getString("token")

        assertThat(token).isNotEmpty
        assertTrue(jwtTokenProvider.validateToken(token))
        val member = memberRepository.findByEmail("test@test.com")
        assertThat(member?.name).isEqualTo("test")
    }

    @Test
    fun login() {
        val loginRequest =
            LoginRequest(
                "test@test.com",
                "12345678",
            )

        val token =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/members/login")
                .then().extract().body().jsonPath().getString("token")

        assertThat(token).isNotEmpty
        assertTrue(jwtTokenProvider.validateToken(token))
    }
}
