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
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw AuthorizationException("Authorization header must start with 'Bearer '")
        }
        return authHeader.substring(BEARER_PREFIX_LENGTH)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val BEARER_PREFIX_LENGTH = 7
    }
}
