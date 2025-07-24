package ecommerce.dto

data class CartItemRequest(
    val productId: Long
)

data class UpdateQuantityRequest(
    val quantity: Int
)
