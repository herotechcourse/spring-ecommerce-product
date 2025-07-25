package ecommerce.dto.cart

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class AddToCartRequest(
    @field:NotNull(message = "ProductId cannot be null.")
    @field:Positive(message = "ProductId must be positive")
    val productId: Long,
    @field:NotNull(message = "Please fill all the fields")
    @field:Min(1)
    val quantity: Int,
)
