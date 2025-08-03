package ecommerce.config

import ecommerce.annotation.Protected
import ecommerce.exception.UnauthorizedException
import ecommerce.service.AuthService
import ecommerce.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val authService: AuthService,
    private val memberService: MemberService,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler is HandlerMethod) {
            val method = handler.method

            val hasAnnotation = method.getAnnotation(Protected::class.java) != null

            if (hasAnnotation) {
                val authHeader = request.getHeader("Authorization") ?: throw IllegalArgumentException("Missing token")
                val userEmail = authService.extractAndValidateToken(authHeader)
                val member = memberService.findByEmail(userEmail) ?: throw UnauthorizedException()
                request.setAttribute("member", member)
            }
        }

        return true
    }
}
