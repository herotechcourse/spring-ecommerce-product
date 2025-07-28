package ecommerce.cart.controller

import ecommerce.auth.annotation.LoginMember
import ecommerce.cart.dto.CartRequest
import ecommerce.cart.dto.CartResponse
import ecommerce.cart.service.CartService
import ecommerce.member.domain.Member
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart-items")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @Valid @RequestBody request: CartRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val response = cartService.addToCart(member, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getCartItems(
        @LoginMember member: Member,
    ): ResponseEntity<List<CartResponse>> {
        val cartItems = cartService.getCartItems(member)
        return ResponseEntity.ok(cartItems)
    }

    @DeleteMapping("/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member,
    ): ResponseEntity<Unit> {
        cartService.removeFromCart(member, productId)
        return ResponseEntity.noContent().build()
    }
}
