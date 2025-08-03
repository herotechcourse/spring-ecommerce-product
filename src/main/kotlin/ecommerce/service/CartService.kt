package ecommerce.service

import ecommerce.model.CartItem
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
) {
    fun getCart(memberId: Long?): List<CartItem> = cartRepository.findByMemberId(memberId)

    fun addProduct(
        memberId: Long?,
        productId: Long,
        quantity: Int,
    ) {
        val existing = cartRepository.findByMemberIdAndProductId(memberId, productId)
        if (existing != null) {
            cartRepository.updateQuantity(memberId, productId, existing.quantity + quantity)
        } else {
            cartRepository.insert(memberId, productId, quantity)
        }
    }

    fun removeProduct(
        memberId: Long?,
        productId: Long,
    ) {
        cartRepository.delete(memberId, productId)
    }
}
