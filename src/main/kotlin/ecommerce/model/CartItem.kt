package ecommerce.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

data class CartItem(
    val id: Long? = null,
    val cartId: Long,
    val productId: Long,
    @field:NotNull
    @field:Positive
    val quantity: Int,
    val product: Product? = null,
    var addedAt: LocalDateTime = LocalDateTime.now(),
)
