package ecommerce.helper

import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.MacAlgorithm
import java.util.Base64
import javax.crypto.SecretKey

class JwtTestSetup(
    val secretKey: String,
    val expireLength: Long,
    val algorithm: MacAlgorithm,
) {
    val key: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))
}
