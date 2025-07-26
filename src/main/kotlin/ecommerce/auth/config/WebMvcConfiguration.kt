package ecommerce.auth.config

import ecommerce.auth.interceptor.CheckLoginInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(CheckLoginInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/api/members/register",
                "/api/members/login",
                "/api/me/token",
                "/auth/login",
                "/auth/registration",
                "/",
            )
    }
}
