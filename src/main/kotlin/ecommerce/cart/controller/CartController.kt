package ecommerce.cart.controller

import ecommerce.auth.annotation.LoggedMember
import ecommerce.auth.model.Member
import ecommerce.cart.model.Cart
import ecommerce.cart.model.CartDTO
import ecommerce.cart.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("/")
    fun getCart(
        @LoggedMember member: Member,
    ): ResponseEntity<Cart> {
        val cart = cartService.getOrCreateCart(member.id!!)
        return ResponseEntity.ok(cart)
    }

    @GetMapping("/mycart")
    fun getCartDto(
        @LoggedMember member: Member,
    ): ResponseEntity<CartDTO> {
        val cartDto = cartService.getCartDTO(member.id!!)
        return ResponseEntity.ok(cartDto)
    }

    @PostMapping("/items")
    fun addItem(
        @LoggedMember member: Member,
        @RequestParam productId: Long,
        @RequestParam quantity: Int,
    ): ResponseEntity<Cart> {
        val cart = cartService.addItem(member.id!!, productId, quantity)
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/items/{productId}")
    fun removeItem(
        @LoggedMember member: Member,
        @PathVariable productId: Long,
    ): ResponseEntity<Cart> {
        val cart = cartService.removeItem(member.id!!, productId)
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/clear")
    fun clearCart(
        @LoggedMember member: Member,
    ): ResponseEntity<Void> {
        cartService.clear(member.id!!)
        return ResponseEntity.noContent().build()
    }
}
