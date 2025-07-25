package ecommerce.configuration

import ecommerce.exception.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AdminOnlyInterceptor() : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val role = request.getAttribute("role") as? String
        if (role != "ADMIN") {
            throw UnauthorizedException("Admin access required")
        }
        return true
    }
}
