package ecommerce.service

import ecommerce.exception.NotFoundException
import ecommerce.model.Cart
import ecommerce.dto.CartDTO
import ecommerce.store.CartStore
import ecommerce.store.ProductStore
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartStore: CartStore,
    private val memberService: MemberService,
    private val productStore: ProductStore,
) {
    fun getOrCreateCart(memberId: Long): Cart {
        if (memberService.findMemberById(memberId) == null) {
            throw NotFoundException("Member does not exist")
        }
        return cartStore.findCartByMemberId(memberId) ?: cartStore.createCart(memberId)
    }

    fun getCartDTO(memberId: Long): CartDTO {
        val cart = getOrCreateCart(memberId)

        val populatedItems =
            cart.items.map { item ->
                val product = productStore.findProductById(item.productId)
                item.copy(product = product)
            }
        val enrichedCart = cart.copy(items = populatedItems)
        return CartDTO.Companion.from(enrichedCart)
    }

    fun addItem(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Cart {
        if (memberService.findMemberById(memberId) == null) {
            throw NotFoundException("Member does not exist")
        }
        if (productStore.findProductById(productId) == null) {
            throw NotFoundException("Product does not exist")
        }
        val cart = getOrCreateCart(memberId)
        cartStore.addCartItem(cart.id, productId, quantity)
        return cartStore.findCartByMemberId(memberId)!!
    }

    fun removeItem(
        memberId: Long,
        productId: Long,
    ): Cart {
        if (memberService.findMemberById(memberId) == null) {
            throw NotFoundException("Member does not exist")
        }
        if (productStore.findProductById(productId) == null) {
            throw NotFoundException("Product does not exist")
        }
        val cart = cartStore.findCartByMemberId(memberId) ?: return throw IllegalArgumentException("Cart not found")
        cartStore.removeCartItem(cart.id, productId)
        return cartStore.findCartByMemberId(memberId)!!
    }

    fun clear(memberId: Long) {
        if (memberService.findMemberById(memberId) == null) {
            throw NotFoundException("Member does not exist")
        }
        val cart = cartStore.findCartByMemberId(memberId) ?: return
        cartStore.clearCart(cart.id)
    }
}
