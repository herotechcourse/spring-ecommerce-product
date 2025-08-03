package ecommerce.model

import java.time.LocalDateTime

class CartItem(
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
    val addedAt: LocalDateTime,
)
