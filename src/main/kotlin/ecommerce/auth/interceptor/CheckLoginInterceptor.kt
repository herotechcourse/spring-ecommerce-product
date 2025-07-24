package ecommerce.auth.interceptor

import ecommerce.auth.exception.AuthorizationException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class CheckLoginInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any)
    : Boolean {
        val accessToken = request.getHeader("Authorization")
            ?: throw AuthorizationException()
        if (!accessToken.startsWith("Bearer ")) {
            throw AuthorizationException()
        }
        return true
    }
}