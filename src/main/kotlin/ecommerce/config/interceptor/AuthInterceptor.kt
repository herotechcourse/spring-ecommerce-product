package ecommerce.config.interceptor

import ecommerce.infrastructure.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class AuthInterceptor(
    jwtTokenProvider: JwtTokenProvider,
) : BaseAuthInterceptor(jwtTokenProvider) {
    override fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        email: String,
    ): Boolean {
        return true
    }
}
