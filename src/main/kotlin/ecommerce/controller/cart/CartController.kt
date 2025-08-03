package ecommerce.controller.cart

import ecommerce.dto.cartItem.AddCartItemRequest
import ecommerce.dto.cartItem.CartItemResponse
import ecommerce.dto.cartItem.CartResponse
import ecommerce.model.Member
import ecommerce.service.CartService
import ecommerce.utils.LoginMember
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
@RequestMapping("/api/cart/items")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping("")
    fun addProductToCart(
        @Valid @RequestBody request: AddCartItemRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartItemResponse> {
        val cartItem = cartService.addProductToCart(member.id, request)
        return ResponseEntity(cartItem, HttpStatus.OK)
    }

    @GetMapping("")
    fun getCartItems(
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val cart = cartService.getCartItems(member.id)
        return ResponseEntity(cart, HttpStatus.OK)
    }

    @DeleteMapping("/{productId}")
    fun removeProductFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member,
    ): ResponseEntity<Void> {
        cartService.removeProductFromCart(member.id, productId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("")
    fun removeAllProductsFromCart(
        @LoginMember member: Member,
    ): ResponseEntity<Void> {
        cartService.clearCart(member.id)
        return ResponseEntity.noContent().build()
    }
}
