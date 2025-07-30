package ecommerce.configuration

import ecommerce.exception.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtAuthInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val accessToken = request.getHeader("Authorization")?.substringAfter("Bearer ") ?: throw UnauthorizedException("Invalid Token")
        if (!jwtTokenProvider.validateToken(accessToken)) throw UnauthorizedException("Invalid Token")

        val email = jwtTokenProvider.getEmailFromToken(accessToken) ?: throw UnauthorizedException("Invalid Token")
        request.setAttribute("email", email)
        return true
    }
}
