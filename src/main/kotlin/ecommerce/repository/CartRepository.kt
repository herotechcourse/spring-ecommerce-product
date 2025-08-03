package ecommerce.repository

import ecommerce.model.CartItem

interface CartRepository {
    fun findByMemberId(memberId: Long?): List<CartItem>

    fun findByMemberIdAndProductId(
        memberId: Long?,
        productId: Long,
    ): CartItem?

    fun insert(
        memberId: Long?,
        productId: Long,
        quantity: Int,
    )

    fun updateQuantity(
        memberId: Long?,
        productId: Long,
        quantity: Int,
    )

    fun delete(
        memberId: Long?,
        productId: Long,
    )
}
