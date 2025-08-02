package ecommerce.service

import ecommerce.dto.MemberResponse
import ecommerce.dto.TopProductStatResponse
import ecommerce.model.CartItem
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class CartService(
    val cartRepository: CartRepository,
    private val productService: ProductService,
) {
    fun addProductToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): CartItem {
        productService.validateProductId(productId)

        var cartItem = getCartItemForMemberAndProduct(memberId, productId)
        val currentTimestamp = Timestamp(System.currentTimeMillis())

        if (cartItem != null) {
            val newQuantity = cartItem.quantity + quantity
            cartItem.updatedAt = currentTimestamp
            return cartRepository.updateQuantity(cartItem, newQuantity)
        }

        cartItem =
            CartItem(
                0,
                memberId,
                productId,
                quantity,
                createdAt = currentTimestamp,
                updatedAt = currentTimestamp,
            )

        return cartRepository.insert(cartItem)
    }

    fun getCartItemForMemberAndProduct(
        memberId: Long,
        productId: Long,
    ): CartItem? {
        return cartRepository.findByMemberAndProductIds(memberId, productId).firstOrNull()
    }

    fun getCartItemsForMember(memberId: Long): List<CartItem> {
        return cartRepository.findByMemberId(memberId)
    }

    fun removeItemFromCart(
        memberId: Long,
        productId: Long,
    ): Int? {
        productService.validateProductId(productId)
        return cartRepository.deleteByMemberAndProduct(memberId, productId)
    }

    fun getTop5AddedProducts(): List<TopProductStatResponse> {
        return cartRepository.findTopAddedProducts(5, 30)
    }

    fun getRecentActiveMembersInTheLast7Days(): List<MemberResponse> {
        return cartRepository.getRecentActiveMembers(7)
    }
}
