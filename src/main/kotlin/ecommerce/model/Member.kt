package ecommerce.model

data class Member (
    var id: Long? = null,
    var email: String,
    var password: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require("@" in email && ".com" in email) { "Email should contain a @ and a .com" }
        require( password.isNotBlank()) { "Password cannot be blank" }
        require(password.length >= 4) { "Password with a minimum of 4  and maximum of 8 characters long" }
        require(password.length <= 8) { "Password with a minimum of 4  and maximum of 8 characters long" }
    }

    companion object {
        fun toEntity(
            member: Member,
        ): Member {
            return Member(member.id, member.email, member.password)
        }
    }
}
