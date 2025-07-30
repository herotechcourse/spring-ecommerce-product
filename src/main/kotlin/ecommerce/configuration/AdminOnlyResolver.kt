package ecommerce.configuration

import ecommerce.annotation.AdminOnly
import ecommerce.exception.ForbiddenException
import ecommerce.exception.UnauthorizedException
import ecommerce.repository.MemberRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AdminOnlyResolver(
    private val memberRepository: MemberRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AdminOnly::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val request =
            webRequest.getNativeRequest(HttpServletRequest::class.java)
                ?: throw UnauthorizedException("No HttpServletRequest")
        val email =
            request.getAttribute("email") as? String ?: throw UnauthorizedException("No authenticated member found 1")
        val member = memberRepository.findByEmail(email) ?: throw UnauthorizedException("No authenticated member found 2")
        if (member.role != "ADMIN") throw ForbiddenException("Forbidden access. Admin only")
        return member.toDto()
    }
}
