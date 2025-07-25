package ecommerce.controller

import ecommerce.auth.annotation.LoginMember
import ecommerce.dto.CartRequest
import ecommerce.model.Member
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {

    @PostMapping
    fun addToCart(
        @RequestBody request: CartRequest,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        cartService.addToCart(member.id, request.productId)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getCart(
        @LoginMember member: Member
    ): ResponseEntity<Any> {
        val cartItems = cartService.getCartItems(member.id)
        return ResponseEntity.ok(cartItems)
    }

    @DeleteMapping
    fun removeFromCart(
        @RequestBody request: CartRequest,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        cartService.removeFromCart(member.id, request.productId)
        return ResponseEntity.noContent().build()
    }
}
