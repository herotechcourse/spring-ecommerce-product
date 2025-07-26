package ecommerce.domain

import java.time.LocalDateTime

data class CartEvent(
    var id: Long = 0,
    val memberId: Long = 0L,
    val productId: Long = 0L,
    var quantityAdded: Int,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
