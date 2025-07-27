package ecommerce.service

import ecommerce.exception.auth.UnauthorizedException
import ecommerce.model.Member
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember

@Component
class LoginMemberArgumentResolver(
    private val memberService: MemberService,
) : HandlerMethodArgumentResolver {
    private val logger = LoggerFactory.getLogger(LoginMemberArgumentResolver::class.java)

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val supports =
            parameter.hasParameterAnnotation(LoginMember::class.java) &&
                parameter.parameterType == Member::class.java
        logger.debug("supportsParameter for {}: {}", parameter.parameterName, supports)
        return supports
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val authentication: Authentication =
            SecurityContextHolder.getContext().authentication
                ?: throw UnauthorizedException("No authenticated user found in SecurityContext.")
        logger.info("Authentication object in resolver: {}", authentication)
        val userPrincipal =
            authentication.principal as? User
                ?: throw UnauthorizedException("Authenticated principal is not a Spring Security User object or is anonymous.")
        val memberEmail = userPrincipal.username
        logger.info("Authenticated member email from principal: {}", memberEmail)

        val member =
            memberService.findByEmail(memberEmail)
                ?: throw UnauthorizedException("Authenticated principal is not a Spring Security User object or is anonymous.")
        logger.info("Fetched member from DB: {}", member)
        return member
    }
}
