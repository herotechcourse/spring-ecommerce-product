package ecommerce.dto.cart

import jakarta.validation.constraints.PositiveOrZero

data class CartRequest(
    @field:PositiveOrZero(message = "Quantity can not be zero")
    val quantity: Int,
)
