package ecommerce.model

data class CartItem(
    var id: Long? = null,
    var memberId: Long,
    var productId: Long,
    var quantity: Int,
)
