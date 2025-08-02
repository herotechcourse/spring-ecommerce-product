package ecommerce.service

import ecommerce.helper.MemberTestFixture.Cases.MEMBER_GURI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.Base64

@SpringBootTest
class JwtProviderTest {
    private lateinit var jwtProvider: JwtProvider

    @BeforeEach
    fun setUp() {
        jwtProvider =
            JwtProvider().apply {
                secretKey =
                    Base64.getEncoder()
                        .encodeToString("my-super-secret-key-my-super-cute-dog-key".toByteArray())
                expireLength = 3600000
            }
    }

    @Test
    fun `should create and validate token`() {
        val member = MEMBER_GURI
        val token = jwtProvider.createToken(member)

        assertThat(jwtProvider.validateToken(token)).isTrue()
        assertThat(jwtProvider.getPayload(token)).isEqualTo("1")
    }

    @Test
    fun `should return false for expired token`() {
        val member = MEMBER_GURI

        jwtProvider.expireLength = -1000
        val token = jwtProvider.createToken(member)

        assertThat(jwtProvider.validateToken(token)).isFalse()
    }

    @Test
    fun `should return false for invalid token`() {
        val member = MEMBER_GURI
        val token = jwtProvider.createToken(member)
        val invalidToke = token.replaceFirstChar { 'a' }

        assertThat(jwtProvider.validateToken(invalidToke)).isFalse()
    }
}
