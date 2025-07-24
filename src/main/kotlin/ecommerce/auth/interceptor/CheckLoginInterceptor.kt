package ecommerce.auth.interceptor

import ecommerce.auth.exception.AuthorizationException
import ecommerce.auth.extractor.AuthorizationExtractor
import ecommerce.auth.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class CheckLoginInterceptor(
    private val authService: AuthService,
    private val authorizationExtractor: AuthorizationExtractor<String>,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val token =
            try {
                authorizationExtractor.extract(request)
            } catch (e: AuthorizationException) {
                throw AuthorizationException("Failed to extract token: ${e.message}")
            }
        authService.findMemberByToken(token)
        return true
    }
}
