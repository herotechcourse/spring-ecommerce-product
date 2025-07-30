package ecommerce.model

data class Cart(
    val id: Long,
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
)
