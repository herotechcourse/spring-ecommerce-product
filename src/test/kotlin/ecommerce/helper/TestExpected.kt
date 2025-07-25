package ecommerce.helper

import ecommerce.dto.MemberRequest
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.Date

class TestExpected(val request: MemberRequest, val set: ValidationTestSet, val id: Long = 1) {
    val accessToken: String = obtainAccessToken()
    val claims: Claims = decode(accessToken, set)

    private fun obtainAccessToken(): String {
        val now = Date()
        val validity = Date(now.time + set.validityInMilliseconds)

        return Jwts.builder()
            .subject(id.toString())
            .claim("email", request.email)
            .issuedAt(now)
            .expiration(validity)
            .signWith(set.secretKey, set.algorithm)
            .compact()
    }

    companion object {
        fun decode(
            token: String,
            set: ValidationTestSet,
        ): Claims {
            return Jwts.parser()
                .verifyWith(set.secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        }
    }
}
