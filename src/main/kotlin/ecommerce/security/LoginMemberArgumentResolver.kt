package ecommerce.security

import ecommerce.annotation.LoginMember
import ecommerce.exception.UnauthorizedException
import ecommerce.service.AuthService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(
    private val jwtTokenProvider: JwtTokenProvider,
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
        val token =
            webRequest.getHeader("Authorization")
                ?.removePrefix("Bearer ")
                ?: throw UnauthorizedException("Authorization header is missing or malformed.")

        if (!jwtTokenProvider.validateToken(token)) {
            throw UnauthorizedException("Invalid or expired access token.")
        }

        val memberIdString = jwtTokenProvider.getSubjectFromToken(token)
        val memberId =
            memberIdString.toLongOrNull()
                ?: throw UnauthorizedException("Invalid token subject (member ID).")

        return authService.getMemberById(memberId)
            ?: throw UnauthorizedException("Authenticated member not found in database.")
    }
}
