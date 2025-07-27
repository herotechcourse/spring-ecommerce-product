package ecommerce.dao

import ecommerce.model.CartItem

interface CartDAO {
    fun addItemToCart(memberId: Long, productId: Long, quantity: Int = 1): Long

    fun getCartItemsByMemberId(memberId: Long): List<CartItem>

    fun removeItemFromCart(memberId: Long, productId: Long): Int

    fun updateItemQuantityInCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Int
}
