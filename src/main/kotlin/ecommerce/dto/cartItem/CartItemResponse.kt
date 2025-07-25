package ecommerce.dto.cartItem

class CartItemResponse(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double,
)
