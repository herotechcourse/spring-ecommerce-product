package ecommerce.controller

import ecommerce.auth.LoginMember
import ecommerce.dto.AddToCartRequest
import ecommerce.model.CartItem
import ecommerce.model.Member
import ecommerce.service.CartService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @Valid @RequestBody request: AddToCartRequest,
        @LoginMember member: Member,
    ): ResponseEntity<Unit> {
        cartService.addProduct(member.id, request.productId, request.productCount)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getCart(
        @LoginMember member: Member,
    ): ResponseEntity<List<CartItem>> {
        return ResponseEntity.ok(cartService.getCart(member.id))
    }

    @DeleteMapping("/{productId}")
    fun deleteFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member,
    ): ResponseEntity<Unit> {
        cartService.removeProduct(member.id, productId)
        return ResponseEntity.noContent().build()
    }
}
