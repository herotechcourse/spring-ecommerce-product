package ecommerce.auth.helper

import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.MacAlgorithm
import java.util.Base64
import javax.crypto.SecretKey

class ValidationTestSet(
    val key: String,
    val validityInMilliseconds: Long,
    val algorithm: MacAlgorithm,
) {
    val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key))
}
