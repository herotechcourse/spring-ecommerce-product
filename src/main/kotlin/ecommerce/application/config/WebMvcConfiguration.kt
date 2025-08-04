package ecommerce.application.config

import ecommerce.application.JwtTokenProvider
import ecommerce.application.resolver.LoggedMemberArgumentResolver
import ecommerce.application.interceptor.CheckLoginInterceptor
import ecommerce.service.MemberService
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
                "/",
                "/api/members/register",
                "/api/members/login",
                "/api/me/token",
                "/auth/login",
                "/auth/signup",
                "/shop",
                "/cart",
                "/admin/**",
                "/api/stats/**",
            )
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(LoggedMemberArgumentResolver(memberService))
    }
}
