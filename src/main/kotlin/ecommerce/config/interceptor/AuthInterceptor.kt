package ecommerce.config.interceptor

import ecommerce.infrastructure.JwtProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class AuthInterceptor(
    jwtProvider: JwtProvider,
) : BaseAuthInterceptor(jwtProvider) {
    override fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        email: String,
    ): Boolean {
        return true
    }
}
