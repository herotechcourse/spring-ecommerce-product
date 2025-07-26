package ecommerce.cart.controller

import ecommerce.auth.application.AuthService
import ecommerce.auth.infrastructure.AuthorizationExtractor
import ecommerce.auth.infrastructure.BearerAuthorizationExtractor
import ecommerce.cart.model.Cart
import ecommerce.cart.service.CartService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
    private val authService: AuthService,
) {
    private val authorizationExtractor: AuthorizationExtractor<String> = BearerAuthorizationExtractor()

    @GetMapping("/")
    fun getCart(request: HttpServletRequest): ResponseEntity<Cart> {
        val token = authorizationExtractor.extract(request)
        val memberId = authService.findMemberByToken(token).id
        val cart = cartService.getOrCreateCart(memberId)
        return ResponseEntity.ok(cart)
    }

    @PostMapping("/items")
    fun addItem(
        request: HttpServletRequest,
        @RequestParam productId: Long,
        @RequestParam quantity: Int,
    ): ResponseEntity<Cart> {
        val token = authorizationExtractor.extract(request)
        val memberId = authService.findMemberByToken(token).id
        val cart = cartService.addItem(memberId, productId, quantity)
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/items/{productId}")
    fun removeItem(
        request: HttpServletRequest,
        @PathVariable productId: Long,
    ): ResponseEntity<Cart> {
        val token = authorizationExtractor.extract(request)
        val memberId = authService.findMemberByToken(token).id
        val cart = cartService.removeItem(memberId, productId)
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/clear")
    fun clearCart(request: HttpServletRequest): ResponseEntity<Void> {
        val token = authorizationExtractor.extract(request)
        val memberId = authService.findMemberByToken(token).id
        cartService.clear(memberId)
        return ResponseEntity.noContent().build()
    }
}
