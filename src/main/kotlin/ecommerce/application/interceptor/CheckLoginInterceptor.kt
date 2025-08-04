package ecommerce.application.interceptor

import ecommerce.application.AuthorizationExtractor
import ecommerce.application.BearerAuthorizationExtractor
import ecommerce.application.JwtTokenProvider
import ecommerce.exception.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class CheckLoginInterceptor(private val jwtTokenProvider: JwtTokenProvider) : HandlerInterceptor {
    private val authorizationExtractor: AuthorizationExtractor<String> = BearerAuthorizationExtractor()

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val token = authorizationExtractor.extract(request)
        if (token.isBlank() || !jwtTokenProvider.validateToken(token)) throw UnauthorizedException("Invalid token")
        val userEmail = jwtTokenProvider.getPayload(token)
        request.setAttribute("email", userEmail)
        return true
    }
}
