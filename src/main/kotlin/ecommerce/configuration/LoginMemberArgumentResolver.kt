package ecommerce.configuration

import ecommerce.annotation.LoginMember
import ecommerce.dto.MemberDto
import ecommerce.repository.MemberRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.naming.AuthenticationException

@Component
class LoginMemberArgumentResolver(
    private val memberRepository: MemberRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): MemberDto {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java) ?: throw AuthenticationException("No HttpServletRequest")
        val email = request.getAttribute("email") as? String ?: throw AuthenticationException("No authenticated member found 1")
        val member = memberRepository.findByEmail(email) ?: throw AuthenticationException("No authenticated member found 2")
        return member.toDto()
    }
}
