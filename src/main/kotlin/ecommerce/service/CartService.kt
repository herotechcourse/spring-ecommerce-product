package ecommerce.service

import ecommerce.entity.CartItem
import ecommerce.repository.CartRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CartService(private val cartRepository: CartRepository) {

    fun addItem(userId: Long, productId: Long) {
        val existing = cartRepository.findByUserIdAndProductId(userId, productId)
        if (existing != null) {
            val newQuantity = existing.quantity + 1
            cartRepository.updateQuantity(userId, productId, newQuantity)
        } else {
            val newItem = CartItem(
                userId = userId,
                productId = productId,
                quantity = 1
            )
            cartRepository.create(newItem)
        }
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
