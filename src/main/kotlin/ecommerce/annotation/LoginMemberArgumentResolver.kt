package ecommerce.annotation

import ecommerce.dto.MemberDto
import ecommerce.exception.NotFoundException
import ecommerce.exception.UnauthorizedException
import ecommerce.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

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
        lateinit var member: MemberDto
        val request =
            webRequest.getNativeRequest(HttpServletRequest::class.java)
                ?: throw UnauthorizedException()

        val email = request.getAttribute("email") ?: throw UnauthorizedException()
        try {
            member = authService.findMember(email.toString())
        } catch (e: NotFoundException) {
            throw UnauthorizedException()
        }

        return member
    }
}
