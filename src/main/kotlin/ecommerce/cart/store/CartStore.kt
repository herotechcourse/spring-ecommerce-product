package ecommerce.cart.store

import ecommerce.cart.model.Cart
import ecommerce.cart.model.CartItem

interface CartStore {
    fun findCartByMemberId(memberId: Long): Cart?

    fun createCart(memberId: Long): Cart

    fun addCartItem(
        cartId: Long,
        productId: Long,
        quantity: Int,
    ): CartItem

    fun removeCartItem(
        cartId: Long,
        productId: Long,
    ): Boolean

    fun clearCart(cartId: Long)

    fun getItems(cartId: Long): List<CartItem>
}
