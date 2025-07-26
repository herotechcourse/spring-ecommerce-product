package ecommerce.config

import ecommerce.exception.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import ecommerce.infrastructure.AuthorizationExtractor
import ecommerce.service.AuthService


class AdminAuthInterceptor(
    private val authExtractor: AuthorizationExtractor,
    private val authService: AuthService
): HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val token = authExtractor.extract(request)
        if (token.isEmpty()) throw UnauthorizedException()
        val admin = authService.findMember(token)
        if (admin.role != "admin") throw UnauthorizedException()
        return true
    }
}
