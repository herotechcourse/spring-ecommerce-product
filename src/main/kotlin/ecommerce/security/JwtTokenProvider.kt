package ecommerce.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val secretKeyString: String,
    @Value("\${jwt.expire-length}")
    private val validityInMs: Long,
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secretKeyString.toByteArray(StandardCharsets.UTF_8))

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as User
        val now = Date()
        val expiryDate = Date(now.time + validityInMs)
        val roles = authentication.authorities.map { it.authority }

        return Jwts.builder()
            .subject(userPrincipal.username)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication? {
        return try {
            val claims: Claims =
                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .payload

            val email = claims.subject
            val roles =
                (claims["roles"] as? Iterable<*>)
                    ?.filterIsInstance<String>()
                    ?.map { SimpleGrantedAuthority(it) } ?: emptyList()
            val principal = User(email, "", roles)
            UsernamePasswordAuthenticationToken(principal, token, roles)
        } catch (ex: JwtException) {
            println("JWT authentication error: ${ex.message}")
            null
        } catch (ex: IllegalArgumentException) {
            println("JWT illegal argument error: ${ex.message}")
            null
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            true
        } catch (e: JwtException) {
            println("JWT validation failed: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            println("JWT validation failed(illegal argument: ${e.message}")
            false
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        // Check if Authorization header is present and starts with "Bearer "
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7) // Extract the token part
        } else {
            null
        }
    }
}
