package ecommerce.controller

import ecommerce.dto.cart.AddToCartRequest
import ecommerce.dto.cart.UpdateQuantityRequest
import ecommerce.model.Cart
import ecommerce.service.CartService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api")
@RestController
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("/cart-items")
    fun getCartItems(request: HttpServletRequest): List<Cart> {
        val userId = request.getAttribute("userId") as Long
        return cartService.getCartItems(userId)
    }

    @PostMapping("/cart-items")
    fun addToCart(
        @Valid @RequestBody addToCartRequest: AddToCartRequest,
        request: HttpServletRequest,
    ): ResponseEntity<Cart> {
        val userId = request.getAttribute("userId") as Long
        val cart = cartService.addToCart(userId, addToCartRequest)
        return ResponseEntity.created(URI.create("/api/cart-items")).body(cart)
    }

    @PutMapping("/cart-items/{productId}")
    fun updateQuantity(
        @PathVariable productId: Long,
        @Valid @RequestBody updateRequest: UpdateQuantityRequest,
        request: HttpServletRequest,
    ): Cart {
        val userId = request.getAttribute("userId") as Long
        return cartService.updateQuantity(userId, productId, updateRequest)
    }

    @DeleteMapping("/cart-items/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Unit> {
        val userId = request.getAttribute("userId") as Long
        cartService.removeFromCart(userId, productId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/cart-items")
    fun clearCart(request: HttpServletRequest): ResponseEntity<Unit> {
        val userId = request.getAttribute("userId") as Long
        cartService.clearCart(userId)
        return ResponseEntity.noContent().build()
    }
}
