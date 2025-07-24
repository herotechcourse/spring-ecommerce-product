package ecommerce.auth

import ecommerce.auth.exception.AuthorizationException
import ecommerce.auth.extractor.AuthorizationExtractor
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class BearerAuthorizationExtractor : AuthorizationExtractor<String> {
    override fun extract(request: HttpServletRequest): String {
        val authHeader = request.getHeader(AuthorizationExtractor.AUTHORIZATION)
            ?: throw AuthorizationException("Missing Authorization header")
        if (!authHeader.startsWith("Bearer ")) {
            throw AuthorizationException("Authorization header must start with 'Bearer '")
        }
        return authHeader.substring(7)
    }
}