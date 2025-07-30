package ecommerce.service

import ecommerce.domain.Cart
import ecommerce.domain.CartItem
import ecommerce.exception.CartOperationException
import ecommerce.exception.ResourceNotFoundException
import ecommerce.repository.CartItemRepository
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productService: ProductService,
) {
    private fun getOrCreateCartForMember(memberId: Long): Cart {
        require(memberId > 0) { "memberId must be greater than 0" }

        return cartRepository.findByMemberId(memberId) ?: run {
            val newCart =
                Cart(
                    memberId = memberId,
                    createdAt = LocalDateTime.now(),
                )
            cartRepository.create(newCart)
            newCart
        }
    }

    fun getCart(memberId: Long): Cart {
        return getOrCreateCartForMember(memberId)
    }

    fun getCartItems(memberId: Long): List<CartItem> {
        val cart = getOrCreateCartForMember(memberId)
        val cartItems = cartItemRepository.findByCartId(cart.id)

        return cartItems
    }

    fun addProductToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Cart {
        require(quantity > 0) { "quantity must be greater than 0" }

        val product = productService.getProductById(productId)
        val cart = getOrCreateCartForMember(memberId)
        val existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.id, product.id)

        if (existingCartItem != null) {
            val newQuantity = existingCartItem.quantity + quantity
            if (newQuantity > product.quantity) {
                throw CartOperationException(
                    "Cannot add more than available stock for product: ${product.name}. Available: ${product.quantity} products on stock.",
                )
            }

            existingCartItem.quantity = newQuantity
            cartItemRepository.update(existingCartItem)
        } else {
            if (quantity > product.quantity) {
                throw CartOperationException("Cannot add more than available stock for product: ${product.name}")
            }

            val newCartItem =
                CartItem(
                    cartId = cart.id,
                    productId = productId,
                    quantity = quantity,
                )

            cartItemRepository.create(newCartItem)
        }
        return cart
    }

    fun removeProductFromCart(
        memberId: Long,
        productId: Long,
    ) {
        val cart =
            cartRepository.findByMemberId(memberId)
                ?: throw ResourceNotFoundException("Cart", "memberId", memberId)
        cartItemRepository.findByCartIdAndProductId(cart.id, productId)
            ?: throw ResourceNotFoundException("Cart Item", "productId", productId)

        cartItemRepository.deleteByCartIdAndProductId(cart.id, productId)
    }
}
