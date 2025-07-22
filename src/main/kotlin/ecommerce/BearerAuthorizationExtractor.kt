package ecommerce

import jakarta.servlet.http.HttpServletRequest

class BearerAuthorizationExtractor : AuthorizationExtractor<String> {
    override fun extract(request: HttpServletRequest): String {
        val authHeader = request.getHeader(AuthorizationExtractor.AUTHORIZATION)
            ?: throw AuthorizationException()
        if (!authHeader.startsWith("Bearer ")) {
            throw AuthorizationException()
        }
        return authHeader.substring(7)
    }
}