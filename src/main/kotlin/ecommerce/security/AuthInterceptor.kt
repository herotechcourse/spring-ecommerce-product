package ecommerce.security

import ecommerce.exception.ForbiddenException
import ecommerce.exception.UnauthorizedException
import ecommerce.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authService: AuthService,
) : HandlerInterceptor {
    companion object {
        const val AUTHENTICATED_MEMBER = "authenticatedMember"

        private val PATH_ROLE_REQUIREMENTS =
            mapOf(
                "/api/admin/products" to listOf("ADMIN"),
                "/api/products" to listOf("USER", "ADMIN"),
                "/api/cart" to listOf("USER", "ADMIN"),
            )
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val token =
            request.getHeader("Authorization")
                ?.removePrefix("Bearer ")
                ?: throw UnauthorizedException("Authorization header is missing or malformed.")

        if (!jwtTokenProvider.validateToken(token)) {
            throw UnauthorizedException("Invalid or expired access token.")
        }

        val memberIdString = jwtTokenProvider.getSubjectFromToken(token)
        val memberId =
            memberIdString.toLongOrNull()
                ?: throw UnauthorizedException("Invalid token subject (member ID).")

        val member =
            authService.getMemberById(memberId)
                ?: throw UnauthorizedException("Authenticated member not found in database.")

        request.setAttribute(AUTHENTICATED_MEMBER, member)

        val requestURI = request.requestURI

        val requiredRolesForPath =
            PATH_ROLE_REQUIREMENTS.entries
                .filter { (pathPrefix, _) -> requestURI.startsWith(pathPrefix) }
                .maxByOrNull { it.key.length }
                ?.value

        if (requiredRolesForPath != null) {
            if (requiredRolesForPath.isNotEmpty() && !requiredRolesForPath.contains(member.role)) {
                throw ForbiddenException("User role '${member.role}' is not authorized to access this resource.")
            }
        } else {
        }

        return true
    }
}
