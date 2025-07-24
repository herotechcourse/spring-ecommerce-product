package ecommerce.cart.service

import ecommerce.cart.domain.CartItem
import ecommerce.cart.dto.CartRequest
import ecommerce.cart.dto.CartResponse
import ecommerce.cart.repository.CartRepository
import ecommerce.member.domain.Member
import ecommerce.product.repository.ProductRepository
import jakarta.validation.ValidationException
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) {
    fun addToCart(member: Member, request: CartRequest): CartResponse {
        val productId = request.productId ?: throw ValidationException("Product ID must not be null")
        val product = productRepository.findById(productId)
            ?: throw ValidationException("Product not found")
        val cartItem = CartItem(
            memberId = member.id ?: throw ValidationException("Member ID must not be null"),
            productId = productId
        )
        cartRepository.insert(cartItem)
        return CartResponse(cartItem.id!!, product)
    }

    fun getCartItems(member: Member): List<CartResponse> {
        val memberId = member.id ?: throw ValidationException("Member ID must not be null")
        val cartItems = cartRepository.findByMemberId(memberId)
        return cartItems.map { cartItem ->
            val product = productRepository.findById(cartItem.productId)
                ?: throw ValidationException("Product not found")
            CartResponse(cartItem.id!!, product)
        }
    }

    fun removeFromCart(member: Member, productId: Long) {
        val memberId = member.id ?: throw ValidationException("Member ID must not be null")
        if (!cartRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw ValidationException("Product not found in cart")
        }
        cartRepository.deleteByMemberIdAndProductId(memberId, productId)
    }
}