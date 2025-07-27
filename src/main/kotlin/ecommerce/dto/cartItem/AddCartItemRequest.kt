package ecommerce.dto.cartItem

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class AddCartItemRequest(
    @field:NotNull(message = "Product ID cannot be null")
    @field:Min(1, message = "Product ID must be positive")
    val productId: Long,
    @field:NotNull(message = "Quantity cannot be null")
    @field:Min(1, message = "Quantity must be at least 1")
    val quantity: Int,
)
