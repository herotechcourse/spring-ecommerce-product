package ecommerce.auth.resolver

import ecommerce.auth.annotation.LoginMember
import ecommerce.auth.extractor.BearerAuthorizationExtractor
import ecommerce.auth.service.AuthService
import ecommerce.member.domain.Member
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
    private val authorizationExtractor: BearerAuthorizationExtractor,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java) && parameter.parameterType == Member::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Member {
        val request =
            webRequest.getNativeRequest(HttpServletRequest::class.java)
                ?: throw IllegalStateException("Invalid request")
        val token = authorizationExtractor.extract(request)
        return authService.findMemberEntityByToken(token)
    }
}
