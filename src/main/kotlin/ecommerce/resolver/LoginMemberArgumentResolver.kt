package ecommerce.resolver

import ecommerce.model.Member
import ecommerce.service.AuthService
import ecommerce.service.MemberService
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember

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
        val userEmail = authService.extractAndValidateToken(webRequest.getHeader("Authorization") ?: "")
        val member = memberService.findByEmail(userEmail) ?: throw UnauthorizedException()
        return member
    }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException : RuntimeException("Unauthorized")
