package ecommerce.cart.controller

import ecommerce.auth.annotation.LoginMember
import ecommerce.auth.exception.AuthorizationException
import ecommerce.cart.dto.CartRequest
import ecommerce.cart.dto.CartResponse
import ecommerce.cart.service.CartService
import ecommerce.member.domain.Member
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/wishes")
class WishController(
    private val cartService: CartService
) {


    @PostMapping
    fun addToCart(
        @Valid @RequestBody request: CartRequest,
        @LoginMember member: Member
    ): ResponseEntity<CartResponse> {
        if (member.id == null) throw AuthorizationException("Member ID cannot be null")
        val response = cartService.addToCart(member, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getCartItems(
        @LoginMember member: Member
    ): ResponseEntity<List<CartResponse>> {
        if (member.id == null) throw AuthorizationException("Member ID cannot be null")
        val cartItems = cartService.getCartItems(member)
        return ResponseEntity.ok(cartItems)
    }

    @DeleteMapping("/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        if (member.id == null) throw AuthorizationException("Member ID cannot be null")
        cartService.removeFromCart(member, productId)
        return ResponseEntity.noContent().build()
    }
}