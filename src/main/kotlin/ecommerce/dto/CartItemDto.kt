package ecommerce.dto

class CartItemDto(
    val quantity: Long,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
)

