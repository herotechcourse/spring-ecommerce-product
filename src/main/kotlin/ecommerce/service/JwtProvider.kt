package ecommerce.service

import ecommerce.entity.Member
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
class JwtProvider {
    lateinit var secretKey: String
    var expireLength: Long = 0

    private val algorithm = Jwts.SIG.HS256
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))
    }

    @PostConstruct
    fun init() {
        println("secretKey in JwtProvider = $secretKey")
    }

    fun createToken(member: Member): String {
        val now = Date()
        val validity = Date(now.time + expireLength)

        return Jwts.builder()
            .subject(member.id.toString())
            .claim("email", member.email)
            .issuedAt(now)
            .expiration(validity)
            .signWith(key, algorithm)
            .compact()
    }

    fun getPayload(token: String): String {
        val claims =
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims =
                Jwts.parser()
                    .verifyWith(key)
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
