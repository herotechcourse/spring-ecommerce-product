package ecommerce.domain

class NewMember(
    val email: String,
    val password: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(password.isNotBlank()) { "Password cannot be blank" }
    }
}
