package ecommerce.domain

data class Member(var userId: Long = 0, val userName: String, val email: String, val passwordHash: String, val role: String)
