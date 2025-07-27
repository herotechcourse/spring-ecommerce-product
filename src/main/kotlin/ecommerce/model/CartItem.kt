package ecommerce.model

data class CartItem (
    var id: Long,
    var memberId: Long,
    var productId: Long,
    var quantity: Int
)
