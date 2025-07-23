package ecommerce.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}")
    secret: String
)  {

    private val secretKey: Key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
    private val expirationMillis: Long = 1000 * 60 * 60 * 24

    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationMillis)

        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact()
    }
}
