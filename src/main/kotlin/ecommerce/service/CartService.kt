package ecommerce.service

import ecommerce.dto.CartItem
import ecommerce.dto.TopProductStatResponse
import ecommerce.repository.CartRepository
import ecommerce.repository.ProductStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    @Qualifier("jdbcProductStore") private val productRepository: ProductStore,
) {
    fun addToCart(
        memberId: Long,
        productId: Long,
    ) {
        productRepository.findById(productId)
            ?: throw NoSuchElementException("Product not found")
        cartRepository.add(memberId, productId)
    }

    fun removeFromCart(
        memberId: Long,
        productId: Long,
    ) {
        return cartRepository.remove(memberId, productId)
    }

    fun getCartItems(memberId: Long): List<CartItem> {
        return cartRepository.getCartItems(memberId)
    }

    fun findTop5ProductsInLast30Days(): List<TopProductStatResponse> {
        return cartRepository.findTop5ProductsInLast30Days()
    }
}
