package ecommerce.dto

import java.time.LocalDateTime

class CartResponse(
    val cartItemId: Long,
    val productId: Long,
    val quantity: Int,
    val updatedAt: LocalDateTime,
)
