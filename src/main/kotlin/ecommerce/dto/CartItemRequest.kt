package ecommerce.dto

data class CartItemRequest(
    val productId: Long,
    val quantity: Int,
)

data class UpdateQuantityRequest(
    val quantity: Int,
)
