package ecommerce.service

import ecommerce.entity.CartItem
import ecommerce.entity.CartItemHistory
import ecommerce.repository.CartItemHistoryRepository
import ecommerce.repository.CartRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CartService(private val cartRepository: CartRepository, private val cartItemHistoryRepository: CartItemHistoryRepository,) {

    fun addItem(userId: Long, productId: Long, quantity: Int) {
        val existingItem = cartRepository.findByUserId(userId)
            .find { it.productId == productId }

        if (existingItem != null) {
            cartRepository.updateQuantity(userId, productId, existingItem.quantity + quantity)
        } else {
            cartRepository.create(CartItem(0, userId, productId, quantity))
        }

        cartItemHistoryRepository.insert(
            CartItemHistory(
                userId = userId,
                productId = productId,
                quantity = quantity
            )
        )
    }

    fun getCart(userId: Long): List<CartItem> {
        return cartRepository.findByUserId(userId)
    }

    fun removeItem(userId: Long, productId: Long) {
        val deleted = cartRepository.deleteByUserIdAndProductId(userId, productId)
        if (!deleted) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found")
        }
    }

    fun updateQuantity(userId: Long, productId: Long, quantity: Int) {
        val existing = cartRepository.findByUserIdAndProductId(userId, productId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found")

        if (quantity == 0) {
            cartRepository.deleteByUserIdAndProductId(userId, productId)
        } else {
            cartRepository.updateQuantity(userId, productId, quantity)
        }
    }
}
