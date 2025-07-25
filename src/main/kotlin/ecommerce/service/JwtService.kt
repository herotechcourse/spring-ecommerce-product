package ecommerce.service

import ecommerce.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}")
    secret: String,
    private val userRepository: UserRepository,
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
    private val expirationMillis: Long = 1000 * 60 * 60 * 24

    fun generateToken(email: String): String {
        val user = userRepository.getByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        val now = Date()
        val expiry = Date(now.time + expirationMillis)

        return Jwts.builder()
            .subject(email)
            .claim("role", user.role)
            .claim("userId", user.id)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact()
    }


    fun validateAndExtractEmail(token: String): String {
        try {
            val claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)

            return claimsJws.payload.subject
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token")
        }
    }
}
