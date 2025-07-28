package ecommerce.security

import ecommerce.advice.GlobalExceptionHandler
import ecommerce.exception.ForbiddenException
import ecommerce.exception.UnauthorizedException
import ecommerce.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authService: AuthService,
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    companion object {
        const val AUTHENTICATED_MEMBER = "authenticatedMember"

        private val PATH_ROLE_REQUIREMENTS =
            mapOf(
                "/api/admin/products/" to listOf("ADMIN"),
                "/api/admin/reports/" to listOf("ADMIN"),
                "/api/products/" to listOf("USER", "ADMIN"),
                "/api/cart/" to listOf("USER", "ADMIN"),
            )
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or malformed.")
            throw UnauthorizedException("Unauthorized.")
        }

        val token = authorizationHeader.removePrefix("Bearer ")

        if (!jwtTokenProvider.validateToken(token)) {
            logger.warn("Invalid or expired access token.")
            throw UnauthorizedException("Unauthorized.")
        }

        val memberIdString = jwtTokenProvider.getSubjectFromToken(token)
        val memberId = memberIdString.toLongOrNull()
        if (memberId == null) {
            logger.warn("Invalid or expired access token.")
            throw UnauthorizedException("Unauthorized.")
        }

        val member = authService.getMemberById(memberId)
        if (member == null) {
            logger.warn("Authenticated member not found in database.")
            throw UnauthorizedException("Unauthorized")
        }

        request.setAttribute(AUTHENTICATED_MEMBER, member)

        val requestURI = request.requestURI

        val requiredRolesForPath =
            PATH_ROLE_REQUIREMENTS.entries
                .filter { (pathPrefix, _) -> requestURI.startsWith(pathPrefix) }
                .maxByOrNull { it.key.length }
                ?.value

        if (requiredRolesForPath != null) {
            if (requiredRolesForPath.isNotEmpty() && !requiredRolesForPath.contains(member.role)) {
                logger.warn("User role '${member.role}' is not authorized to access this resource.")
                throw ForbiddenException("Forbidden.")
            }
        }

        return true
    }
}
