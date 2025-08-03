package ecommerce.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class JwtTokenProviderTest {
    private lateinit var tokenProvider: JwtTokenProvider

    private val secretKey = "my-super-secret-key-for-jwt-signing-1234567890"
    private val expirationMs = 3600000L // 1 hour

    @BeforeEach
    fun setUp() {
        tokenProvider = JwtTokenProvider(secretKey, expirationMs)
    }

    @Test
    fun `should create and parse token correctly`() {
        val email = "user@example.com"
        val token = tokenProvider.createToken(email)

        assertNotNull(token)
        assertTrue(token.isNotBlank())

        val payload = tokenProvider.getPayload(token)
        assertEquals(email, payload)
    }

    @Test
    fun `should validate valid token`() {
        val token = tokenProvider.createToken("test@domain.com")

        val isValid = tokenProvider.validateToken(token)

        assertTrue(isValid)
    }

    @Test
    fun `should not validate invalid token`() {
        val invalidToken = "this.is.not.valid.jwt"

        val isValid = tokenProvider.validateToken(invalidToken)

        assertFalse(isValid)
    }

    @Test
    fun `should not validate expired token`() {
        val shortLivedProvider = JwtTokenProvider(secretKey, 1L)
        val token = shortLivedProvider.createToken("expired@test.com")

        val isValid = shortLivedProvider.validateToken(token)

        assertFalse(isValid)
    }
}
