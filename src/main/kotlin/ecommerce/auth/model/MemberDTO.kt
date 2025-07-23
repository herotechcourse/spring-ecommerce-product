package ecommerce.auth.model

data class MemberDTO(
    val id: Long,
    val email: String,
    val password: String,
) {
    companion object {
        fun from(member: Member): MemberDTO {
            return MemberDTO(
                id = member.id ?: 0,
                email = member.email,
                password = member.password,
            )
        }
    }
}
