package ecommerce.service

import ecommerce.dto.CartUpdateResult
import ecommerce.repository.CartItemRepository
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
) {
    fun addProductToCart(
        userId: Long,
        productId: Long,
        quantity: Int,
    ): CartUpdateResult {
        cartRepository.findCartByMemberId(userId) ?: cartRepository.createCart(userId)
        val newCart = cartRepository.findCartByMemberId(userId)
        val existingItem = cartItemRepository.findByCartIdAndProductId(newCart!!.id, productId)

        when (existingItem) {
            null -> {
                cartItemRepository.addProductToCart(productId, newCart.id, quantity)
                return CartUpdateResult.PRODUCT_ADDED
            }
            else -> {
                cartItemRepository.updateQuantityByCartIdAndProductId(
                    newCart.id,
                    productId,
                    existingItem.quantity + quantity,
                )
                return CartUpdateResult.PRODUCT_QUANTITY_UPDATED
            }
        }
    }
}
