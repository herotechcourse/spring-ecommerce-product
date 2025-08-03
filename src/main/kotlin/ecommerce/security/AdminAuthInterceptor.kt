package ecommerce.security

import ecommerce.exception.auth.ForbiddenException
import ecommerce.exception.auth.UnauthorizedException
import ecommerce.model.MemberRole
import ecommerce.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AdminAuthInterceptor(
    private val memberService: MemberService,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val authentication: Authentication =
            SecurityContextHolder.getContext().authentication
                ?: throw UnauthorizedException("Authentication required for admin access.")

        val userPrincipal =
            authentication.principal as? User
                ?: throw UnauthorizedException("Authenticated principal is not a Spring Security User object or is anonymous.")

        val memberEmail = userPrincipal.username

        val member =
            memberService.findByEmail(memberEmail)
                ?: throw UnauthorizedException("Authenticated member data not found in database for admin access.")

        if (member.role != MemberRole.ROLE_ADMIN) {
            throw ForbiddenException("Access denied. Admin role required.")
        }
        return true
    }
}
