package ecommerce.controller

import ecommerce.domain.Member
import ecommerce.exception.ForbiddenException
import ecommerce.exception.UnauthorizedException
import ecommerce.security.AuthInterceptor
import ecommerce.security.JwtTokenProvider
import ecommerce.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@DisplayName("Auth Interceptor Unit Tests")
class AuthInterceptorTest {
    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Mock
    private lateinit var authService: AuthService

    @InjectMocks
    private lateinit var authInterceptor: AuthInterceptor

    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse

    private fun mockRequest(
        uri: String,
        authorizationHeader: String? = null,
    ): HttpServletRequest {
        val mockRequest = mock(HttpServletRequest::class.java)
        `when`(mockRequest.requestURI).thenReturn(uri)
        `when`(mockRequest.getHeader("Authorization")).thenReturn(authorizationHeader)

        return mockRequest
    }

    private fun mockMember(
        id: Long,
        email: String,
        role: String,
    ): Member {
        return Member(userId = id, userName = "bo", email = email, passwordHash = "hashedPass", role = role)
    }

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        response = mock(HttpServletResponse::class.java)
    }

    @Test
    fun `Should throw UnauthorizedException for missing token`() {
        request = mockRequest("/api/products/")
        assertThrows<UnauthorizedException> {
            authInterceptor.preHandle(request, response, Any())
        }.also { e ->
            assertThat(e.message).contains("Unauthorized")
        }
        verifyNoInteractions(jwtTokenProvider)
        verifyNoInteractions(authService)
    }

    @Test
    fun `Should throw UnauthorizedException for malformed token header`() {
        request = mockRequest("/api/products/", "WrongToken")
        assertThrows<UnauthorizedException> {
            authInterceptor.preHandle(request, response, Any())
        }.also { e ->
            assertThat(e.message).contains("Unauthorized.")
        }
        verifyNoInteractions(jwtTokenProvider)
        verifyNoInteractions(authService)
    }

    @Test
    fun `should throw ForbiddenException when user role is not authorized for admin path`() {
        val token = "some.token"
        val authorizationHeader = "Bearer $token"
        val memberId = 1L
        val userMember = mockMember(memberId, "bo@gmail.com", "USER")
        val days = 30
        request = mockRequest("/api/admin/reports/top-products?days=$days", authorizationHeader)

        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getSubjectFromToken(token)).thenReturn(memberId.toString())
        `when`(authService.getMemberById(memberId)).thenReturn(userMember)

        assertThrows<ForbiddenException> {
            authInterceptor.preHandle(request, response, Any())
        }.also { e ->
            assertThat(e.message).contains("Forbidden.")
        }
        verify(request).setAttribute(AuthInterceptor.AUTHENTICATED_MEMBER, userMember)
    }

    @Test
    fun `Should return true when user role is authorized for a user path`() {
        val token = "some.token"
        val authorizationHeader = "Bearer $token"
        val memberId = 1L
        val userMember = mockMember(memberId, "bo@gmail.com", "USER")
        request = mockRequest("/api/products/123", authorizationHeader)

        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getSubjectFromToken(token)).thenReturn(memberId.toString())
        `when`(authService.getMemberById(memberId)).thenReturn(userMember)

        val result = authInterceptor.preHandle(request, response, Any())
        assertThat(result).isTrue()
        verify(request).setAttribute(AuthInterceptor.AUTHENTICATED_MEMBER, userMember)
    }

    @Test
    fun `should return true when admin role is authorized for admin path`() {
        val token = "some.token"
        val authorizationHeader = "Bearer $token"
        val memberId = 2L
        val adminMember = mockMember(memberId, "admin@gmail.com", "ADMIN")
        val days = 30
        request = mockRequest("/api/admin/reports/top-products?days=$days", authorizationHeader)

        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getSubjectFromToken(token)).thenReturn(memberId.toString())
        `when`(authService.getMemberById(memberId)).thenReturn(adminMember)

        val result = authInterceptor.preHandle(request, response, Any())
        assertThat(result).isTrue()
        verify(request).setAttribute(AuthInterceptor.AUTHENTICATED_MEMBER, adminMember)
    }

    @Test
    fun `should handle path prefix matching correctly (longer prefix takes precedence)`() {
        val token = "some.token"
        val authorizationHeader = "Bearer $token"
        val memberId = 4L
        val days = 30

        val adminMember = mockMember(memberId, "admin@gmail.com", "ADMIN")
        request = mockRequest("/api/admin/reports/top-products?days=$days", authorizationHeader)
        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getSubjectFromToken(token)).thenReturn(memberId.toString())
        `when`(authService.getMemberById(memberId)).thenReturn(adminMember)
        val resultAdmin = authInterceptor.preHandle(request, response, Any())
        assertThat(resultAdmin).isTrue()
        verify(request).setAttribute(
            AuthInterceptor.AUTHENTICATED_MEMBER,
            adminMember,
        )

        val request2 = mockRequest("/api/admin/reports/top-products?days=$days", authorizationHeader)
        val userMember = mockMember(memberId, "bo@gmail.com", "USER")

        reset(jwtTokenProvider, authService)

        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getSubjectFromToken(token)).thenReturn(memberId.toString())
        `when`(authService.getMemberById(memberId)).thenReturn(userMember)

        assertThrows<ForbiddenException> {
            authInterceptor.preHandle(request2, response, Any())
        }.also { e ->
            assertThat(e.message).contains("Forbidden.")
        }
        verify(request2).setAttribute(AuthInterceptor.AUTHENTICATED_MEMBER, userMember)
    }

    @Test
    fun `should correctly set AUTHENTICATED_MEMBER in request`() {
        val token = "some.token"
        val authorizationHeader = "Bearer $token"
        val memberId = 3L
        val testMember = mockMember(memberId, "test@example.com", "USER")
        request = mockRequest("/api/admin/cart", authorizationHeader)

        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getSubjectFromToken(token)).thenReturn(memberId.toString())
        `when`(authService.getMemberById(memberId)).thenReturn(testMember)

        authInterceptor.preHandle(request, response, Any())

        verify(request).setAttribute(AuthInterceptor.AUTHENTICATED_MEMBER, testMember)
    }
}
