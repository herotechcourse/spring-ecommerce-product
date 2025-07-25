package ecommerce.cart.model

data class Cart(
    val id: Long,
    val memberId: Long,
    val items: List<CartItem> = listOf(),
)
