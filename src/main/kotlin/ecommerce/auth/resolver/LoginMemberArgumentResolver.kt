package ecommerce.auth.resolver

import ecommerce.auth.annotation.LoginMember
import ecommerce.auth.exception.AuthorizationException
import ecommerce.auth.service.AuthService
import ecommerce.member.repository.MemberRepository
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(
    private val authService: AuthService,
    private val memberRepository: MemberRepository
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
        val token = webRequest.getHeader("Authorization")?.removePrefix("Bearer ")
            ?: throw AuthorizationException("Missing Authorization header")
        val memberResponse = try {
            authService.findMemberByToken(token)
        } catch (e: AuthorizationException) {
            throw AuthorizationException("Failed to authenticate token: ${e.message}")
        }
        val member = memberRepository.findById(memberResponse.id)
            ?: throw AuthorizationException("Member not found with id: ${memberResponse.id}")
        if (member.email.isNullOrEmpty()) {
            throw AuthorizationException("Member email cannot be null or empty for id: ${memberResponse.id}")
        }
        return member
    }
}