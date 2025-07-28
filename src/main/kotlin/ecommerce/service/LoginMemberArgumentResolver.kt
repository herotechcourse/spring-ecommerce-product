package ecommerce.service

import ecommerce.dto.LoginMember
import ecommerce.exception.UnauthorizedException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(
    private val authService: AuthService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val token = extractToken(webRequest)

        return authService.findMemberByToken(token)
    }

    private fun extractToken(webRequest: NativeWebRequest): String {
        val authHeader =
            webRequest.getHeader("Authorization")
                ?: throw UnauthorizedException("Authorization header is missing")

        return authHeader.removePrefix("Bearer ").trim()
            .takeIf { it.isNotEmpty() }
            ?: throw UnauthorizedException("Bearer token is missing")
    }
}
