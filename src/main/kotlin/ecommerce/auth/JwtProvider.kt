package ecommerce.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

object JwtProvider {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private const val EXPIRATION_TIME_MS = 3600_000 // 1 hour

    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + EXPIRATION_TIME_MS)

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val now = Date()
            val claims =
                Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
            claims.body.expiration.after(now)
        } catch (e: Exception) {
            false
            throw RuntimeException(e)
        }
    }

    fun getEmailFromToken(token: String): String {
        val claims =
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

        return claims.body.subject
    }
}
