package ecommerce.controller

import ecommerce.annotation.LoginMember
import ecommerce.dto.CartItemRequest
import ecommerce.dto.CartItemResponse
import ecommerce.dto.CartUpdateResult
import ecommerce.dto.MemberDto
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
        @LoginMember member: MemberDto,
    ): ResponseEntity<CartUpdateResult> {
        val addToCartResult = cartService.addProductToCart(member.id, request.productId, request.quantity)
        return ResponseEntity.ok(addToCartResult)
    }

    @GetMapping
    fun getCartItems(
        @LoginMember member: MemberDto,
    ): ResponseEntity<List<CartItemResponse>> {
        val cartItems = cartService.getCartItems(member.id)
        return ResponseEntity.ok(cartItems)
    }

    @DeleteMapping("/{productId}")
    fun deleteCartItems(
        @PathVariable productId: Long,
        @LoginMember member: MemberDto,
    ): ResponseEntity<Void> {
        return when (cartService.deleteProductFromCart(member.id, productId)) {
            true -> ResponseEntity.noContent().build()
            false -> ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/update/quantity")
    fun updateCartItemQuantity(
        @RequestBody request: CartItemRequest,
        @LoginMember member: MemberDto,
    ): ResponseEntity<CartUpdateResult> {
        return when (cartService.updateCartItemQuantity(member.id, request.productId, request.quantity)) {
            true -> ResponseEntity.noContent().build()
            false -> ResponseEntity.notFound().build()
        }
    }
}
