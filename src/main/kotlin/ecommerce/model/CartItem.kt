package ecommerce.model

class CartItem(
    val id: Long,
    val cartId: Long,
    val productId: Long,
    val quantity: Int,
)
