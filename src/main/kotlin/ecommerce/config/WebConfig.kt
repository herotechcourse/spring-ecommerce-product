package ecommerce.config

import ecommerce.config.argumentResolver.LoginAdminArgumentResolver
import ecommerce.config.argumentResolver.LoginMemberArgumentResolver
import ecommerce.config.interceptor.AuthInterceptor
import ecommerce.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val userRepository: UserRepository,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/admin/products/**", "/api/member/cart/**")
        super.addInterceptors(registry)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        val additionalResolvers =
            listOf(
                LoginMemberArgumentResolver(userRepository),
                LoginAdminArgumentResolver(userRepository),
            )
        resolvers.addAll(additionalResolvers)
        super.addArgumentResolvers(resolvers)
    }
}
