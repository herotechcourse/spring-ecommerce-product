package ecommerce.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import ecommerce.infrastructure.AuthorizationExtractor
import ecommerce.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
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
        if (token.isEmpty()){
            response.status = HttpStatus.FORBIDDEN.value()
            return false
        }

        val admin = authService.findMemberByToken(token)
        if (admin.role != "admin")
        {
            response.status = HttpStatus.FORBIDDEN.value()
            return false
        }
        return true
    }
}
