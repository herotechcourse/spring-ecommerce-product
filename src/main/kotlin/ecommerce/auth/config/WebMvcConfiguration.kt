package ecommerce.auth.config

import ecommerce.auth.application.LoggedMemberArgumentResolver
import ecommerce.auth.infrastructure.JwtTokenProvider
import ecommerce.auth.interceptor.CheckLoginInterceptor
import ecommerce.auth.service.MemberService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
    private val memberService: MemberService,
    private val jwtTokenProvider: JwtTokenProvider,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(CheckLoginInterceptor(jwtTokenProvider))
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/api/members/register",
                "/api/members/login",
                "/api/me/token",
                "/auth/login",
                "/auth/registration",
                "/shop/",
                "/cart/",
                "/api/stats/**"
            )
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(LoggedMemberArgumentResolver(memberService))
    }
}
