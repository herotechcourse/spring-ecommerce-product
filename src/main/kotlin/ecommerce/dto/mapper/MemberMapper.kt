package ecommerce.dto.mapper

import ecommerce.dto.TokenResponse

object MemberMapper {
    fun toResponse(token: String): TokenResponse {
        return TokenResponse(token)
    }
}
