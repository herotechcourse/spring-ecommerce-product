package ecommerce.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.util.Date

@DisplayName("JWT Token Provider Unit Tests")
class JwtTokenProviderTest {
    companion object {
        private const val TEST_SECRET_KEY = "ncksdjcksdjbvksbdvuivubkdsiecnlanclvdndbjkvbkdkvb"
        private const val TEST_EXPIRATION_MS = 3600000L
    }

    private lateinit var jwtTokenProvider: JwtTokenProvider

    @BeforeEach
    fun setUp() {
        jwtTokenProvider = JwtTokenProvider(TEST_SECRET_KEY, TEST_EXPIRATION_MS)
    }

    private fun getClaimsFromToken(token: String): Claims {
        val method = JwtTokenProvider::class.java.getDeclaredMethod("getClaims", String::class.java)
        method.isAccessible = true
        return method.invoke(jwtTokenProvider, token) as Claims
    }

    @DisplayName("Unit Tests for Token Generation")
    @Test
    fun `Should generate a non-empty token`() {
        val token = jwtTokenProvider.generateToken(1L, "bo@gmail.com", "USER")
        assertThat(token).isNotBlank()
        assertThat(token.split(".").size).isEqualTo(3)
    }

    @Test
    fun `Should include correct subject (member ID)`() {
        val memberId = 123L
        val token = jwtTokenProvider.generateToken(memberId, "bo@gmail.com", "USER")
        val claims = getClaimsFromToken(token)
        assertThat(claims.subject).isEqualTo(memberId.toString())
    }

    @Test
    fun `Should include correct role claim`() {
        val role = "ADMIN"
        val token = jwtTokenProvider.generateToken(1L, "admin@gmail.com", role)
        val claims = getClaimsFromToken(token)
        assertThat(claims["role"] as String).isEqualTo(role)
    }

    @DisplayName("Unit Tests for Token Validation")
    @Test
    fun `Should return true for a valid, non-expired token`() {
        val token = jwtTokenProvider.generateToken(1L, "bo@gmail.com", "USER")
        assertThat(jwtTokenProvider.validateToken(token)).isTrue()
    }

    @Test
    fun `Should return false for a token with an invalid signature`() {
        val validToken = jwtTokenProvider.generateToken(1L, "bo@gmail.com", "USER")
        val invalidSecretKeyString = "akebjvilbvlibvlyvksdnvkjfsvkfsbvjfbvbvbvbvbksjjjffjf"
        val invalidValidator = JwtTokenProvider(invalidSecretKeyString, TEST_EXPIRATION_MS)

        assertThat(invalidValidator.validateToken(validToken)).isFalse()
    }

    @Test
    fun `Should return false for a malformed token`() {
        val malformedToken = "wrong.jwt"
        assertThat(jwtTokenProvider.validateToken(malformedToken)).isFalse()
    }

    @DisplayName("Unit Tests for Claim Extraction")
    @Test
    fun `Should correctly extract subject from a valid token`() {
        val memberId = 456L
        val token = jwtTokenProvider.generateToken(memberId, "bo@gmail.com", "USER")
        assertThat(jwtTokenProvider.getSubjectFromToken(token)).isEqualTo(memberId.toString())
    }

    @Test
    fun `Should correctly extract role from a valid token`() {
        val role = "ADMIN"
        val token = jwtTokenProvider.generateToken(7L, "admin@gmail.com", role)
        assertThat(jwtTokenProvider.getRoleFromToken(token)).isEqualTo(role)
    }

    @Test
    fun `Should throw exception if the required claim is missing (role)`() {
        val manuallyBuiltTokenWithoutRole =
            Jwts.builder()
                .subject("999")
                .claim("email", "no-role@example.com")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date(System.currentTimeMillis() + TEST_EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(TEST_SECRET_KEY.toByteArray()), Jwts.SIG.HS256)
                .compact()

        val exception =
            assertThrows<IllegalStateException> {
                jwtTokenProvider.getRoleFromToken(manuallyBuiltTokenWithoutRole)
            }
        assertThat(exception.message).contains("Role claim 'role' missing or not a String in token.")
    }
}
