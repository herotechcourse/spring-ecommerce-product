package ecommerce.dto.cartItem

data class CartItemResponse(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double,
)
