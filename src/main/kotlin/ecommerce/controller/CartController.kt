package ecommerce.controller

import ecommerce.annotations.LoginMember
import ecommerce.dto.CartItem
import ecommerce.dto.CartRequest
import ecommerce.dto.MemberResponse
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @RequestBody request: CartRequest,
        @LoginMember member: MemberResponse,
    ): ResponseEntity<Void> {
        cartService.addToCart(member.id, request.productId)
        return ResponseEntity.created(
            URI.create("/api/cart"),
        ).build()
    }

    @DeleteMapping
    fun removeFromCart(
        @RequestBody request: CartRequest,
        @LoginMember member: MemberResponse,
    ): ResponseEntity<Void> {
        cartService.removeFromCart(member.id, request.productId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getCart(
        @LoginMember member: MemberResponse,
    ): List<CartItem> {
        return cartService.getCartItems(member.id)
    }
}
