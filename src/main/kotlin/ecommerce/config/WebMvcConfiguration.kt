package ecommerce.config

import ecommerce.annotation.LoginMemberArgumentResolver
import ecommerce.infrastructure.AuthorizationExtractor
import ecommerce.service.AuthService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
    private val authService: AuthService,
    private val authExtractor: AuthorizationExtractor
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        resolvers.add(LoginMemberArgumentResolver(authService, authExtractor))
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AdminAuthInterceptor(authExtractor, authService))
            .addPathPatterns("/admin/**")
    }
}
