package ecommerce.cart.dto

import jakarta.validation.constraints.NotNull

class CartRequest(
    @field:NotNull(message = "Product ID must not be null")
    var productId: Long?,
)
