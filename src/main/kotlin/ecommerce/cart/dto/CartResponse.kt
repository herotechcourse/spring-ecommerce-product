package ecommerce.cart.dto

import ecommerce.product.domain.Product

class CartResponse(
    val id: Long,
    val product: Product,
)
