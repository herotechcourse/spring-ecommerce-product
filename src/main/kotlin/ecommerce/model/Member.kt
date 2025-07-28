package ecommerce.model

data class Member(
    val id: Long = 0L,
    val email: String,
    val password: String,
    val name: String,
    val role: String = "USER",
)
