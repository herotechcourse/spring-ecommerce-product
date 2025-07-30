package ecommerce.controller

import ecommerce.annotation.LoginMember
import ecommerce.dto.AllCartItemsResponse
import ecommerce.dto.CartItemRequest
import ecommerce.dto.CartItemResponse
import ecommerce.dto.MemberDto
import ecommerce.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/cart")
class CartController(private val cartService: CartService) {
    @GetMapping
    fun getAllItems(
        @LoginMember member: MemberDto,
    ): ResponseEntity<AllCartItemsResponse> {
        val allCartItemsDto = cartService.getAllItems(member.id)
        return ResponseEntity.status(HttpStatus.OK).body(allCartItemsDto)
    }

    @PostMapping
    fun addItem(
        @LoginMember member: MemberDto,
        @RequestBody request: CartItemRequest,
    ): ResponseEntity<CartItemResponse> {
        val cartItemDto = cartService.addItem(member.id, request)
        return ResponseEntity.created(URI("/api/cart/items/${cartItemDto.productId}")).body(cartItemDto)
    }

    @DeleteMapping
    fun deleteItem(
        @LoginMember member: MemberDto,
        @RequestBody request: CartItemRequest,
    ): ResponseEntity<CartItemResponse?> {
        val cartItemDto = cartService.deleteItem(member.id, request)
        if (cartItemDto == null) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok().body(cartItemDto)
    }
}
