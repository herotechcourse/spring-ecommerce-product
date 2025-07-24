package ecommerce.cart.dto

import ecommerce.product.domain.Product

data class CartResponse(
    val id: Long,
    val product: Product
)
