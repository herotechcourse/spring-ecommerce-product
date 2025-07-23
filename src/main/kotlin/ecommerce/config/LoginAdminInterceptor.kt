package ecommerce.config

import ecommerce.enums.UserRole
import ecommerce.exception.UnauthorisedUserException
import ecommerce.infrastructure.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoginAdminInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val bearer = request.getHeader("Authorization") ?: throw UnauthorisedUserException()
        val token = bearer.replace("Bearer ", "").trim()
        if (!jwtTokenProvider.validateToken(token)) throw UnauthorisedUserException("Token not valid")

        val payload = jwtTokenProvider.getPayload(token)
        if (payload.role == UserRole.USER) throw UnauthorisedUserException("Admin role required")

        return true
    }
}
