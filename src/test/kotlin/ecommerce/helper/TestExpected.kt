package ecommerce.helper

import ecommerce.dto.MemberRequest
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.*

class TestExpected(
    val request: MemberRequest,
    val set: JwtTestSetup,
    val id: Long = 1,
) {
    val accessToken: String = obtainAccessToken()
    val claims: Claims = decode(accessToken, set)

    private fun obtainAccessToken(): String {
        val now = Date()
        val validity = Date(now.time + set.expireLength)

        return Jwts.builder()
            .subject(id.toString())
            .claim("email", request.email)
            .issuedAt(now)
            .expiration(validity)
            .signWith(set.key, set.algorithm)
            .compact()
    }

    companion object {
        fun decode(
            token: String,
            setup: JwtTestSetup,
        ): Claims {
            return Jwts.parser()
                .verifyWith(setup.key)
                .build()
                .parseSignedClaims(token)
                .payload
        }
    }
}
