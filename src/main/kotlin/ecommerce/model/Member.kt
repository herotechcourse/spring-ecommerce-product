package ecommerce.model

class Member (
    var email: String,
    var password: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require("@" in email && ".com" in email) { "Email should contain a @ and a .com" }
        require( password.isNotBlank()) { "Password cannot be blank" }
        require(password.length <= 255) { "Password must be at least 4 characters" }
    }

    companion object {
        fun toEntity(
            member: Member,
        ): Member {
            return Member(member.email, member.password)
        }
    }
}
