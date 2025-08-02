package ecommerce.controller

import com.fasterxml.jackson.databind.ObjectMapper
import ecommerce.config.LoginMemberArgumentResolver
import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.LoginFailedException
import ecommerce.exception.MemberAlreadyExistsException
import ecommerce.helper.MemberTestExpected
import ecommerce.helper.MemberTestFixture.Cases
import ecommerce.helper.MemberTestFixture.ValidationCase
import ecommerce.service.MemberService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argForWhich
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(MemberController::class)
class MemberControllerTest
    @Autowired
    constructor(private val mockMvc: MockMvc) {
        @MockitoBean
        private lateinit var memberService: MemberService

        @MockitoBean
        private lateinit var loginMemberArgumentResolver: LoginMemberArgumentResolver

        @Autowired
        private lateinit var objectMapper: ObjectMapper

        @Test
        fun `should not allow duplicate email registration`() {
            val request = MemberRequest(email = "test@email.com", password = "password")

            whenever(memberService.registerByEmail(any<MemberRequest>()))
                .thenThrow(MemberAlreadyExistsException("Email already exists"))

            mockMvc.post("/api/members/register") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isConflict() }
                }
        }

        @Test
        fun `should register admin and return expected token`() {
            val request = Cases.VALID_REQUEST
            val expect = MemberTestExpected(request, ValidationCase.DEFAULT_CASE)
            val expectedResponse = TokenResponse(expect.accessToken)

            whenever(memberService.registerByEmail(any<MemberRequest>()))
                .thenReturn(expectedResponse)

            mockMvc.post("/api/members/register") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isCreated() }
                    jsonPath("$.accessToken") { value(expect.accessToken) }
                }

            verify(memberService).registerByEmail(
                argForWhich {
                    email == request.email && password == request.password
                },
            )
        }

        @Test
        fun `should login and return expected token`() {
            val request = Cases.VALID_REQUEST_GURI
            val expect = MemberTestExpected(request, ValidationCase.DEFAULT_CASE)
            val expectedResponse = TokenResponse(expect.accessToken)

            whenever(memberService.loginByEmail(any<MemberRequest>()))
                .thenReturn(expectedResponse)

            mockMvc.post("/api/members/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.accessToken") { value(expect.accessToken) }
                }

            verify(memberService).loginByEmail(
                argForWhich {
                    email == request.email && password == request.password
                },
            )
        }

        @Test
        fun `should return 401 when logging in with unregistered email`() {
            val request = Cases.VALID_REQUEST_GURI
            val expect = MemberTestExpected(request, ValidationCase.DEFAULT_CASE)
            val expectedResponse = TokenResponse(expect.accessToken)

            whenever(memberService.loginByEmail(any<MemberRequest>()))
                .thenThrow(LoginFailedException())

            mockMvc.post("/api/members/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isUnauthorized() }
                }
        }
    }
