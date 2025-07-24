package ecommerce.api

import ecommerce.auth.infrastructure.JwtTokenProvider
import ecommerce.auth.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = [
    "security.jwt.token.secret-key=U8gX7jK2mZ3cL5bQ9rA1eH4nX2dP7wC8",
    "security.jwt.token.expire-length=3600000"
])
class AuthTest {

    @Autowired
    private lateinit var jwtService: JwtTokenProvider

    @Test
    fun `should generate valid JWT token`() {
        val userDetails = Member(
            email = "test@example.com",
            password = "password",
        )
        val token = jwtService.createToken(userDetails.email)

        assertThat(jwtService.validateToken(token)).isTrue
    }
}

