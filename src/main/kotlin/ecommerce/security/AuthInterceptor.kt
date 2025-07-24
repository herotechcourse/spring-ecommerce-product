package ecommerce.security

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

        return true
    }
}
