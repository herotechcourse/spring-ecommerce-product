package ecommerce.config

import ecommerce.interceptor.AdminInterceptor
import ecommerce.interceptor.AuthInterceptor
import ecommerce.resolver.LoginMemberArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val loginMemberArgumentResolver: LoginMemberArgumentResolver,
    private val adminInterceptor: AdminInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/protected/**")

        registry.addInterceptor(adminInterceptor)
            .addPathPatterns("/admin/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginMemberArgumentResolver)
    }
}
