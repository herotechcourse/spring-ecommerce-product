package ecommerce.model

enum class Role(val role: String) {
    USER("USER"),
    ADMIN("ADMIN"),
}

data class Member(
    var id: Long,
    var email: String,
    var password: String,
    var role: Role,
    var name: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require("@" in email && ".com" in email) { "Email should contain a @ and a .com" }
        require(password.isNotBlank()) { "Password cannot be blank" }
        require(password.length >= 4) { "Password with a minimum of 4  and maximum of 8 characters long" }
        require(password.length <= 8) { "Password with a minimum of 4  and maximum of 8 characters long" }
    }
}
