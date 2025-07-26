package ecommerce.cart.controller

import ecommerce.cart.model.Cart
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
class CartController(private val cartService: CartService) {
    @GetMapping("/{memberId}")
    fun getCart(
        @PathVariable memberId: Long,
    ): ResponseEntity<Cart> {
        val cart = cartService.getOrCreateCart(memberId)
        return ResponseEntity.ok(cart)
    }

    @PostMapping("/{memberId}/items")
    fun addItem(
        @PathVariable memberId: Long,
        @RequestParam productId: Long,
        @RequestParam quantity: Int,
    ): ResponseEntity<Cart> = ResponseEntity.ok(cartService.addItem(memberId, productId, quantity))

    @DeleteMapping("/{memberId}/items/{productId}")
    fun removeItem(
        @PathVariable memberId: Long,
        @PathVariable productId: Long,
    ): ResponseEntity<Cart> = ResponseEntity.ok(cartService.removeItem(memberId, productId))

    @DeleteMapping("/{memberId}/clear")
    fun clearCart(
        @PathVariable memberId: Long,
    ): ResponseEntity<Void> {
        cartService.clear(memberId)
        return ResponseEntity.noContent().build()
    }
}
