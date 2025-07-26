package ecommerce.dto.cartItem

data class CartItemResponse(
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImg: String,
    val quantity: Int
)
