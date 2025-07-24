package ecommerce.controller

import ecommerce.dto.CartItemRequest
import ecommerce.dto.CartUpdateResult
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/wishes")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @RequestBody request: CartItemRequest,
    ): ResponseEntity<CartUpdateResult> {
        val addToCartResult = cartService.addProductToCart(1L, request.productId, request.quantity)
        return ResponseEntity.ok(addToCartResult)
    }
}
