package ecommerce.service

import ecommerce.model.CartItem
import ecommerce.repository.CartRepository
import ecommerce.repository.MemberRepository
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    val cartRepository: CartRepository,
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository,
    private val productService: ProductService,
) {
    fun addProductToCart(memberId: Long, productId: Long, quantity: Int): CartItem {
        productService.validateProductId(productId)

        var cartItem = getCartItemForMemberAndProduct(memberId, productId)

        if (cartItem != null) {
            val newQuantity = cartItem.quantity + quantity
            return cartRepository.updateQuantity(cartItem, newQuantity)
        }

        cartItem = CartItem(
            0,
            memberId,
            productId,
            quantity
        )

        return cartRepository.insert(cartItem)
    }

    fun calculateCartItemTotalPrice(cartItem: CartItem): Double {
        val product = productRepository.get(cartItem.productId)
            ?: throw IllegalArgumentException("Product with ID ${cartItem.productId} not found")

        val cartQuantity = cartItem.quantity.toDouble()
        val productPrice = product.price.toDouble()
        return productPrice * cartQuantity
    }

    fun getCartItemForMemberAndProduct(memberId: Long, productId: Long): CartItem? {
        return cartRepository.findByMemberAndProductIds(memberId, productId).firstOrNull()
    }

    fun getCartItemsForMember(memberId: Long): List<CartItem> {
        return cartRepository.findByMemberId(memberId)
    }

    fun removeItemFromCart(memberId: Long, productId: Long): Int? {
        productService.validateProductId(productId)
        return cartRepository.deleteByMemberAndProduct(memberId, productId)
    }
}
