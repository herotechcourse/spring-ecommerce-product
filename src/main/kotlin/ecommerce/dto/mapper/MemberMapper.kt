package ecommerce.dto.mapper

import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.entity.Member

object MemberMapper {
    fun toResponse(token: String): TokenResponse {
        return TokenResponse(token)
    }
}
