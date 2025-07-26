package ecommerce.cart.model

import ecommerce.products.model.ProductDTO

data class CartItemDTO(
    val id: Long?,
    val cartId: Long,
    val productId: Long,
    val quantity: Int,
    val product: ProductDTO? = null
) {
    companion object {
        fun from(cartItem: CartItem): CartItemDTO {
            return CartItemDTO(
                id = cartItem.id,
                cartId = cartItem.cartId,
                productId = cartItem.productId,
                quantity = cartItem.quantity,
                product = cartItem.product?.let { ProductDTO.from(it) }
            )
        }
    }
}
