package ecommerce.auth

import ecommerce.exception.AuthorizationException
import ecommerce.model.Member
import ecommerce.service.MemberService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(
    private val tokenProvider: JwtTokenProvider,
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
        val token =
            webRequest.getHeader("Authorization")?.removePrefix("Bearer ")
                ?: throw AuthorizationException()
        if (!tokenProvider.validateToken(token)) throw AuthorizationException()
        val email = tokenProvider.getPayload(token)
        val member =
            memberService.findByEmail(email) ?: throw AuthorizationException()
        return member
    }
}
