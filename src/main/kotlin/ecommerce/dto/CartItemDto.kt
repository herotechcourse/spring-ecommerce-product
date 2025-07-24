package ecommerce.dto

class CartItemDto(
    val cartId: Long,
    val userId: Long,
    val productId: Long,
    val quantity: Int,
)
