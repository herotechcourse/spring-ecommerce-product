package ecommerce.resolver

import ecommerce.annotation.LoginMember
import ecommerce.exception.UnauthorizedException
import ecommerce.model.Member
import ecommerce.service.AuthService
import ecommerce.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(
    private val authService: AuthService,
    private val memberService: MemberService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Member {
        val request = webRequest.nativeRequest as HttpServletRequest
        val member = request.getAttribute("member") as? Member

        if (member == null) {
            throw UnauthorizedException()
        }

        return member
    }
}
