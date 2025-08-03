package ecommerce.auth

import ecommerce.exception.AuthorizationException
import ecommerce.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AdminOnlyInterceptor(
    private val tokenProvider: JwtTokenProvider,
    private val memberService: MemberService,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true

        val method = handler.method
        val hasAdminOnly = method.getAnnotation(AdminOnly::class.java) != null
        if (!hasAdminOnly) return true

        val token =
            request.getHeader("Authorization")?.removePrefix("Bearer ")
                ?: throw AuthorizationException()

        if (!tokenProvider.validateToken(token)) {
            return unauthorized(response, "Invalid token")
        }

        val email = tokenProvider.getPayload(token)
        val member =
            memberService.findByEmail(email)
                ?: throw AuthorizationException()

        if (member.role != "ADMIN") {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.writer.write("Access denied: admin only")
            return false
        }

        request.setAttribute("authenticatedMember", member)
        return true
    }

    private fun unauthorized(
        response: HttpServletResponse,
        message: String,
    ): Boolean {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.write(message)
        return false
    }
}
