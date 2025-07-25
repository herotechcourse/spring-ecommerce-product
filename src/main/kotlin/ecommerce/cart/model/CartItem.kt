package ecommerce.cart.model

import ecommerce.products.model.Product
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CartItem(
    val id: Long? = null,
    val cartId: Long,
    val productId: Long,
    @field:NotNull
    @field:Positive
    val quantity: Int,
    val product: Product? = null
)
