package ecommerce.dto

import ecommerce.model.Product

class RawCartItemDto(
    val cartId: Long,
    val quantity: Long,
    val productId: Long,
)
