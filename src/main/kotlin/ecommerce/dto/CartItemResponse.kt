package ecommerce.dto

data class CartItemResponse(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val productPrice: Double,
    val productImageUrl: String,
)
