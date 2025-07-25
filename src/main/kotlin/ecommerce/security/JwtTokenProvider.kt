package ecommerce.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val secretKeyString: String,
    @Value("\${jwt.expiration-ms}")
    private val expirationMs: Long,
) {
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateToken(
        memberId: Long,
        memberEmail: String,
        memberRole: String,
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .subject(memberId.toString())
            .claim("email", memberEmail)
            .claim("role", memberRole)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getSubjectFromToken(token: String): String {
        val claims = getClaims(token)
        return claims.subject
    }

    fun getRoleFromToken(token: String): String {
        val claims = getClaims(token)
        return claims.get("role", String::class.java)
            ?: throw IllegalStateException("Role claim 'role' missing or not a String in token.")
    }

    fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            true
        } catch (e: ExpiredJwtException) {
            println("JWT token is expired: ${e.message}")
            false
        } catch (e: JwtException) {
            println("Invalid JWT token: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            println("JWT token is null, empty, or malformed: ${e.message}")
            false
        }
    }
}
