package ecommerce.service

import ecommerce.entity.Member
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {
    @Value("\${security.jwt.token.secret-key}")
    private lateinit var rawSecretKey: String

    @Value("\${security.jwt.token.expire-length}")
    private var validityInMilliseconds: Long = 0

    private val algorithm = Jwts.SIG.HS256

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(rawSecretKey))
    }

    fun createToken(member: Member): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .subject(member.id.toString())
            .claim("email", member.email)
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey, algorithm)
            .compact()
    }

    fun getPayload(token: String): String {
        val claims =
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        return claims.subject
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
}
