package ecommerce.auth

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider() {
    private var secret = SECRET_KEY
    private var validityInMilliseconds = EXPIRATION_TIME
    private val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun createToken(payload: String): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .subject(payload)
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun getPayload(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims =
                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
            !claims.payload.expiration.before(Date())
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    companion object {
        private const val SECRET_KEY = "X1yk3dV+NQemyyJbg6WzioaRR1NArxZxjMvvroTZauk="
        private const val EXPIRATION_TIME = 3600000L
    }
}
