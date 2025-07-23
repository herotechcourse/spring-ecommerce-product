package ecommerce.dto.cartProduct

data class CartProductDTO(
    val id: Long,
    val cartId: Long,
    val productID: Long,
    val quantity: Int,
)
