package ecommerce.auth.interceptor

import ecommerce.auth.exception.AuthException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class CheckLoginInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val accessToken = request.getHeader("Authorization") ?: throw AuthException("Invalid or not existing token")
        return true
    }
}
