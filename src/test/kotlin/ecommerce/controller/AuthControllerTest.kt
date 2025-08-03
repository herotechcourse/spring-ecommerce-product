package ecommerce.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import ecommerce.auth.AdminOnlyInterceptor
import ecommerce.auth.LoginMemberArgumentResolver
import ecommerce.config.WebConfig
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
import ecommerce.model.Member
import ecommerce.service.AuthService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(
    AuthController::class,
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [WebConfig::class],
        ),
    ],
)
class AuthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var authService: AuthService

    @MockkBean
    private lateinit var adminOnlyInterceptor: AdminOnlyInterceptor

    @MockkBean
    private lateinit var loginMemberArgumentResolver: LoginMemberArgumentResolver

    @Test
    fun `should return token when login is successful`() {
        val request = TokenRequest("user@email.com", "1234")
        val expectedResponse = TokenResponse("mocked-token")

        every {
            authService.login(
                match {
                    it.email == "user@email.com" && it.password == "1234"
                },
            )
        } returns expectedResponse

        every { adminOnlyInterceptor.preHandle(any(), any(), any()) } returns true
        every { loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            Member(
                email = "a@a",
                password = "123",
            )

        mockMvc.post("/api/members/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                json("""{ "token": "mocked-token" }""")
            }
        }
    }

    @Test
    fun `should return token when registration is successful`() {
        val request = TokenRequest("user@email.com", "1234")
        val expectedResponse = TokenResponse("registered-token")

        every {
            authService.register(
                match {
                    it.email == "user@email.com" && it.password == "1234"
                },
            )
        } returns expectedResponse

        every { adminOnlyInterceptor.preHandle(any(), any(), any()) } returns true
        every { loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            Member(
                email = "a@a",
                password = "123",
            )

        mockMvc.post("/api/members/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isEqualTo(201) }
            content {
                json("""{ "token": "registered-token" }""")
            }
        }
    }
}
