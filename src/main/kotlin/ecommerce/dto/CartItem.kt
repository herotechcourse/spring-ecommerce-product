package ecommerce.dto

data class CartItem(
    val productId: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
)
