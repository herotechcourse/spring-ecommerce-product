package ecommerce.entity

class Member(
    val id: Long,
    val email: String,
    val password: String,
) {
    fun matches(actual: String): Boolean = password == actual
}
