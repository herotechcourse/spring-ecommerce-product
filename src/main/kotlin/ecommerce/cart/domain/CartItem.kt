package ecommerce.cart.domain

import java.time.LocalDateTime

data class CartItem(
    var id: Long? = null,
    val memberId: Long,
    val productId: Long,
    val addedAt: LocalDateTime? = LocalDateTime.now(),
)
