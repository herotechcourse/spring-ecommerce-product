package ecommerce.cart.domain

import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

data class CartItem(
    var id: Long? = null,
    @field:Positive(message = "Member ID must be greater than 0")
    val memberId: Long,
    @field:Positive(message = "Product ID must be greater than 0")
    val productId: Long,
    val addedAt: LocalDateTime? = LocalDateTime.now(),
)
