package ecommerce.dto

class CartItemDto(
    var quantity: Long,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
)
