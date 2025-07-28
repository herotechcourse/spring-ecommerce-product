package ecommerce.entity

import java.time.LocalDateTime

class CartItem(
    val id: Long,
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
