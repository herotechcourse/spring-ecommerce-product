package ecommerce.entity

import java.time.LocalDateTime

data class CartItemHistory(
    val id: Long = 0,
    val userId: Long,
    val productId: Long,
    val quantity: Int,
    val action: String = "ADD",
    val createdAt: LocalDateTime? = null,
)
