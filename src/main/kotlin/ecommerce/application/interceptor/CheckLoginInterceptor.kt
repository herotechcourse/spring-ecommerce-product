package ecommerce.application.interceptor

import ecommerce.application.AuthorizationExtractor
import ecommerce.application.BearerAuthorizationExtractor
import ecommerce.application.JwtTokenProvider
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
        println("CheckLoginInterceptor: token=$token")
        if (token.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or empty Authorization token")
            return false
        }
        val userEmail = jwtTokenProvider.getPayload(token)
        request.setAttribute("email", userEmail)
        return true
    }
}
