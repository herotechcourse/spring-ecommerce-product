package ecommerce.service

import ecommerce.domain.Cart
import ecommerce.domain.CartEvent
import ecommerce.domain.CartItem
import ecommerce.dto.cart.CartResponse
import ecommerce.dto.cartItem.CartItemResponse
import ecommerce.exception.CartOperationException
import ecommerce.exception.ResourceNotFoundException
import ecommerce.repository.CartEventRepository
import ecommerce.repository.CartItemRepository
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productService: ProductService,
    private val cartEventRepository: CartEventRepository,
) {
    private fun getOrCreateCartForMember(memberId: Long): Cart {
        if (memberId <= 0) throw IllegalArgumentException("memberId must be greater than 0")

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

    fun getCartItems(memberId: Long): List<CartItemResponse> {
        val cart = getOrCreateCartForMember(memberId)
        val cartItems = cartItemRepository.findByCartId(cart.id)

        return cartItems.mapNotNull { cartItem ->
            val product = productService.getProductById(cartItem.productId)
            CartItemResponse(
                productId = product.id,
                productName = product.name,
                quantity = cartItem.quantity,
                price = product.price,
            )
        }
    }

    fun addProductToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): CartResponse {
        if (quantity <= 0) throw IllegalArgumentException("quantity must be greater than 0")

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
        val cartEvent =
            CartEvent(
                memberId = memberId,
                productId = productId,
                quantityAdded = quantity,
                timestamp = LocalDateTime.now(),
            )
        cartEventRepository.save(cartEvent)
        val updatedCartItems = getCartItems(memberId)

        val totalPrice = updatedCartItems.sumOf { it.price * it.quantity }
        val totalQuantity = updatedCartItems.sumOf { it.quantity }

        return CartResponse(
            id = cart.id,
            memberId = memberId,
            items = updatedCartItems,
            totalPrice = totalPrice,
            totalQuantity = totalQuantity,
        )
    }

    fun removeProductFromCart(
        memberId: Long,
        productId: Long,
    ) {
        val cart =
            cartRepository.findByMemberId(memberId)
                ?: throw ResourceNotFoundException("Cart", "memberId", memberId)
        val cartItem =
            cartItemRepository.findByCartIdAndProductId(cart.id, productId)
                ?: throw ResourceNotFoundException("Cart Item", "productId", productId)

        cartItemRepository.deleteByCartIdAndProductId(cart.id, productId)
    }
}
