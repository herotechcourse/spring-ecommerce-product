package ecommerce.auth.data

object MemberMapper {
    fun toResponse(token: String): TokenResponse {
        return TokenResponse(token)
    }

    fun toEntity(
        request: MemberRequest,
        id: Long,
    ): Member {
        return Member(
            id = id,
            email = request.email,
            password = request.password,
        )
    }
}
