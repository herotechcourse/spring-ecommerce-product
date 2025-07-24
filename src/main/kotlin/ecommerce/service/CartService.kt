package ecommerce.service

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
    ) {
        val cart = cartRepository.findCartByMemberId(userId)
        if (cart == null) {
            cartRepository.createCart(userId)
        }
        cartItemRepository.addProductToCart(productId, cart!!.id, quantity)
        val existingItem = cartItemRepository.findByCartIdAndProductId(cart.id, productId)

        when (existingItem) {
            null -> cartItemRepository.addProductToCart(productId, cart.id, quantity)
            else ->
                cartItemRepository.updateQuantityByCartIdAndProductId(
                    cart.id,
                    productId,
                    existingItem.quantity + quantity,
                )
        }
    }
}
