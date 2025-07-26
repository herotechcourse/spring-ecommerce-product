package ecommerce.annotation

import ecommerce.exception.UnauthorizedException
import ecommerce.infrastructure.AuthorizationExtractor
import ecommerce.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class LoginMemberArgumentResolver(
    private val authService: AuthService,
    private val authExtractor: AuthorizationExtractor
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw UnauthorizedException()

        val token = authExtractor.extract(request)
        if (token.isBlank()) throw UnauthorizedException()

        return authService.findMemberByToken(token)
    }
}
