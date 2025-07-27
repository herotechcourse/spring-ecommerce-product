package ecommerce.dto.cartItem

data class CartResponse(
    val items: List<CartItemResponse>,
    val totalItems: Int,
    val totalPrice: Double,
)
