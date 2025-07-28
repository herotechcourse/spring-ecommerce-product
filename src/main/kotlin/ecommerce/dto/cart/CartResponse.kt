package ecommerce.dto.cart

import ecommerce.dto.cartItem.CartItemResponse

data class CartResponse(
    val id: Long,
    val memberId: Long,
    val items: List<CartItemResponse>,
    val totalPrice: Double,
    val totalQuantity: Int,
)
