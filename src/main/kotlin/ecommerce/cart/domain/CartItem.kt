package ecommerce.cart.domain

data class CartItem(
    var id: Long? = null,
    val memberId: Long,
    val productId: Long,
)
