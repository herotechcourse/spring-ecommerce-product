package ecommerce.auth.extractor

import ecommerce.auth.exception.AuthorizationException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class BearerAuthorizationExtractor {
    fun extract(request: HttpServletRequest): String {
        val authHeader =
            request.getHeader(AUTHORIZATION)
                ?: throw AuthorizationException("Missing Authorization header")
        if (!authHeader.startsWith("Bearer ")) {
            throw AuthorizationException("Authorization header must start with 'Bearer '")
        }
        return authHeader.substring(7)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}
