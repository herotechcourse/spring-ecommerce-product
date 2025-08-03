package ecommerce.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val userRoleInterceptor: UserRoleInterceptor,
    private val loginUserArgumentResolver: LoginUserArgumentResolver,
    private val adminRoleInterceptor: AdminRoleInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userRoleInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns(
                "/api/products",
                "/api/users/register",
                "/api/users/login",
                "/debug/**",
            )

        registry.addInterceptor(adminRoleInterceptor)
            .addPathPatterns("/admin/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserArgumentResolver)
    }
}
