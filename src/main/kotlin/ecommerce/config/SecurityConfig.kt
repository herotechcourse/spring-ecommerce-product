package ecommerce.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Profile("!test-security")
class SecurityConfig {
    // TODO: remove at the end as only for testing purposes with manual request creation
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Disable CSRF for testing or APIs
            .authorizeHttpRequests {
                it.requestMatchers("/api/members/register", "/debug/**").permitAll()
                it.anyRequest().authenticated()
            }
            .httpBasic { } // or formLogin {}

        return http.build()
    }
}
