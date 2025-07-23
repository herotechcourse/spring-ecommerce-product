package ecommerce.configuration

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {
    @Value("\${security.jwt.token.secret-key}")
    private lateinit var secretKeyString: String

    @Value("\${security.jwt.token.expire-length}")
    private var validityInMilliseconds: Long = 0

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray(StandardCharsets.UTF_8))
    }

    fun createToken(email: String): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey)
            .compact()
    }
}
