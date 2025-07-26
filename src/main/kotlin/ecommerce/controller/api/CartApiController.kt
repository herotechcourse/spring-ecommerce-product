package ecommerce.controller.api

import ecommerce.annotation.LoginMember
import ecommerce.domain.Member
import ecommerce.dto.cart.AddToCartRequest
import ecommerce.dto.cart.CartResponse
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
@RequestMapping("/api/cart")
class CartApiController(private val cartService: CartService) {
    @GetMapping
    fun getCart(
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val cartItems = cartService.getCartItems(member.userId)
        val cartEntity = cartService.getCart(member.userId)

        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        val totalQuantity = cartItems.sumOf { it.quantity }

        val cartResponse =
            CartResponse(
                id = cartEntity.id,
                memberId = member.userId,
                items = cartItems,
                totalPrice = totalPrice,
                totalQuantity = totalQuantity,
            )

        return ResponseEntity.ok().body(cartResponse)
    }

    @PostMapping
    fun addProductToCart(
        @Valid @RequestBody request: AddToCartRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val updatedCart =
            cartService.addProductToCart(
                member.userId,
                request.productId,
                quantity = request.quantity,
            )
        return ResponseEntity.ok().body(updatedCart)
    }

    @DeleteMapping("/{id}")
    fun deleteProductFromCart(
        @LoginMember member: Member,
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        cartService.removeProductFromCart(member.userId, id)
        return ResponseEntity.noContent().build()
    }
}
