package ecommerce.controller.api

import ecommerce.dto.CartAddItemForm
import ecommerce.dto.CartUpdateQuantityForm
import ecommerce.exception.InternalServerErrorException
import ecommerce.model.CartItem
import ecommerce.model.Member
import ecommerce.service.CartService
import ecommerce.ui.LoginMember
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

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
) {
    @GetMapping
    fun viewCart(
        @LoginMember member: Member,
    ): ResponseEntity<List<CartItem>> {
        val memberId = member.id ?: throw InternalServerErrorException(MESSAGE_AUTH_FAILED)
        val cartItems = cartService.getCart(memberId)
        return ResponseEntity.ok(cartItems)
    }

    @PostMapping
    fun addToCart(
        @RequestBody @Valid cartForm: CartAddItemForm,
        @LoginMember member: Member,
    ): ResponseEntity<String> {
        val memberId = member.id ?: throw InternalServerErrorException(MESSAGE_AUTH_FAILED)
        return cartService.addToCart(memberId, cartForm.productId, cartForm.quantity)
    }

    @PutMapping("/{productId}")
    fun updateQuantity(
        @PathVariable productId: Long,
        @RequestBody @Valid cartForm: CartUpdateQuantityForm,
        @LoginMember member: Member,
    ): ResponseEntity<String> {
        val memberId = member.id ?: throw InternalServerErrorException(MESSAGE_AUTH_FAILED)
        return cartService.updateQuantity(memberId, productId, cartForm.quantity)
    }

    @DeleteMapping("/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member,
    ): ResponseEntity<String> {
        val memberId = member.id ?: throw InternalServerErrorException(MESSAGE_AUTH_FAILED)
        return cartService.removeFromCart(memberId, productId)
    }

    companion object {
        const val MESSAGE_AUTH_FAILED = "auth failed"
    }
}
