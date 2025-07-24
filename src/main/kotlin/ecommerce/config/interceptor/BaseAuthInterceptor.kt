package ecommerce.config.interceptor

import ecommerce.exception.UnauthorisedUserException
import ecommerce.infrastructure.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

abstract class BaseAuthInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val bearer = request.getHeader("Authorization") ?: throw UnauthorisedUserException()
        val token = bearer.removePrefix("Bearer ").trim()
        if (!jwtTokenProvider.validateToken(token)) throw UnauthorisedUserException("Token not valid")

        val payload = jwtTokenProvider.getPayload(token)
        request.setAttribute("email", payload.email)

        return handleAuthenticatedRequest(request, response, handler, payload.email)
    }

    abstract fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        email: String,
    ): Boolean
}
