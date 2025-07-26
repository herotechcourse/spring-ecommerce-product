package ecommerce.cart.model

data class CartDTO(
    val id: Long,
    val memberId: Long,
    val items: List<CartItemDTO>,
) {
    companion object {
        fun from(cart: Cart): CartDTO {
            return CartDTO(
                id = cart.id,
                memberId = cart.memberId,
                items = cart.items.map { CartItemDTO.from(it) },
            )
        }
    }
}
