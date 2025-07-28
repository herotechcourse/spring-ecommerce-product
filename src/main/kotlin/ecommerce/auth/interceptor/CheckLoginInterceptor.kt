package ecommerce.auth.interceptor

import ecommerce.auth.exception.AuthorizationException
import ecommerce.auth.extractor.BearerAuthorizationExtractor
import ecommerce.auth.service.AuthService
import ecommerce.member.repository.MemberRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class CheckLoginInterceptor(
    private val authService: AuthService,
    private val authorizationExtractor: BearerAuthorizationExtractor,
    private val memberRepository: MemberRepository,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val token =
            try {
                authorizationExtractor.extract(request)
            } catch (e: AuthorizationException) {
                throw AuthorizationException("Failed to extract token: ${e.message}")
            }
        val memberResponse = authService.findMemberByToken(token)
        val member =
            memberRepository.findById(memberResponse.id)
                ?: throw AuthorizationException("Member not found with id: ${memberResponse.id}")

        if (request.requestURI.startsWith("/admin/") && member.role != "ADMIN") {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("""{"error":"Unauthorized: Admin role required"}""")
            return false
        }
        request.setAttribute("member", member)
        return true
    }
}
