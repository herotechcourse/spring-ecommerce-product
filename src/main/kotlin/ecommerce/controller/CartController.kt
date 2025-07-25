package ecommerce.controller

import ecommerce.dto.CartItemDto
import ecommerce.dto.CartItemRequest
import ecommerce.model.Member
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
        @LoginMember member: Member,
    ): ResponseEntity<List<CartItemDto>> {
        // authenticate user -> happens automatically via Interception
        // get items from service
        // return items
        cartService.getAllItems(member.id!!)
        return ResponseEntity.status(HttpStatus.OK).body(emptyList())
    }

    @PostMapping
    fun addItem(
        @LoginMember member: Member,
        @RequestBody request: CartItemRequest,
    ): ResponseEntity<Unit> {
        cartService.addItems(member.id!!, request)
        return ResponseEntity.created(URI("/api/cart/${cartId}/items/${productId}/${quantity}")).build()
    }

    @DeleteMapping
    fun deleteItem(
        @LoginMember member: Member,
        @RequestBody request: CartItemRequest,
    ): ResponseEntity<Unit> {
        cartService.deleteItems(member.id!!, request)
        return ResponseEntity.noContent().build()
    }
}
