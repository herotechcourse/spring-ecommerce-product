package ecommerce.controller.api

import ecommerce.annotation.LoginMember
import ecommerce.domain.Cart
import ecommerce.domain.Member
import ecommerce.dto.cart.AddToCartRequest
import ecommerce.dto.cart.CartResponse
import ecommerce.service.CartService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.times

@RestController
@RequestMapping("/api/cart")
class CartApiController(private val cartService: CartService) {
    private fun createCartResponse(
        cart: Cart,
        memberId: Long,
    ): CartResponse {
        val cartItems = cartService.getCartItems(memberId)
        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        val totalQuantity = cartItems.sumOf { it.quantity }

        return CartResponse(
            id = cart.id,
            memberId = memberId,
            items = cartItems,
            totalPrice = totalPrice,
            totalQuantity = totalQuantity,
        )
    }

    @GetMapping
    fun getCart(
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val cart = cartService.getCart(member.userId)
        val cartResponse = createCartResponse(cart, cart.memberId)
        return ResponseEntity.ok().body(cartResponse)
    }

    @PostMapping
    fun addProductToCart(
        @Valid @RequestBody request: AddToCartRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val updatedCart =
            cartService.addProductToCart(
                member.userId,
                request.productId,
                quantity = request.quantity,
            )
        val cartResponse = createCartResponse(updatedCart, member.userId)
        return ResponseEntity.ok().body(cartResponse)
    }

    @DeleteMapping("/{id}")
    fun deleteProductFromCart(
        @LoginMember member: Member,
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        cartService.removeProductFromCart(member.userId, id)
        return ResponseEntity.noContent().build()
    }
}
