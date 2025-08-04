package ecommerce.model

data class Member(
    val email: String,
    val password: String,
    val name: String,
    val id: Long? = null,
)
