package ecommerce.domain

data class Member(var userId: Long, val userName: String, val email: String, val passwordHash: String, val role: String)
