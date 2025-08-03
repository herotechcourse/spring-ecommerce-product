package ecommerce.dto

import jakarta.validation.constraints.Positive
import org.jetbrains.annotations.NotNull

class AddToCartRequest(
    @field:NotNull
    @field:Positive
    val productId: Long,
    @field:NotNull
    @field:Positive
    val productCount: Int,
)
