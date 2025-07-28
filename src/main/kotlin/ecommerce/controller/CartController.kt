package ecommerce.controller

import ecommerce.dto.CartQuantityRequest
import ecommerce.dto.CartRequest
import ecommerce.dto.CartResponse
import ecommerce.dto.annotation.LoginMember
import ecommerce.dto.mapper.CartMapper
import ecommerce.entity.Member
import ecommerce.service.CartService
import org.springframework.http.HttpStatus
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
    private val service: CartService,
) {
    @GetMapping
    fun getCart(
        @LoginMember member: Member,
    ): ResponseEntity<List<CartResponse>> {
        val cartItems = service.findByMemberId(member.id)
        val cartResponses = cartItems.map { CartMapper.toResponse(it) }
        return ResponseEntity.ok(cartResponses)
    }

    @PostMapping("/items/{productId}")
    fun addCartItem(
        @PathVariable productId: Long,
        @RequestBody request: CartQuantityRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val response = service.upsertCartItem(member.id, productId, request.quantity)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/items")
    fun updateCartItems(
        @RequestBody requests: List<CartRequest>,
        @LoginMember member: Member,
    ): ResponseEntity<List<CartResponse>> {
        val responses = service.upsertCartItems(member.id, requests)
        return ResponseEntity.ok(responses)
    }

    @DeleteMapping("/items/{productId}")
    fun deleteCartItem(
        @PathVariable productId: Long,
        @LoginMember member: Member,
    ): ResponseEntity<Void> {
        service.deleteBy(member.id, productId)
        return ResponseEntity.noContent().build()
    }
}
