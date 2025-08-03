package ecommerce.model

import java.time.LocalDateTime
import java.util.UUID

data class CartItem(
    val id: Long = 0,
    val memberId: UUID,
    val productId: Long,
    val quantity: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
