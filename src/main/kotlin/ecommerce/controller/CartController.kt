package ecommerce.controller

import ecommerce.common.LoginMember
import ecommerce.dto.CartItemRequest
import ecommerce.dto.CartItemResponse
import ecommerce.dto.UpdateQuantityRequest
import ecommerce.entity.User
import ecommerce.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {

    @PostMapping
    fun addToCart(
        @LoginMember user: User,
        @RequestBody request: CartItemRequest
    ): ResponseEntity<Void> {
        cartService.addItem(user.id!!, request.productId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun getCart(
        @LoginMember user: User
    ): ResponseEntity<List<CartItemResponse>> {
        val items = cartService.getCart(user.id!!)
        val response = items.map { CartItemResponse(it.productId, it.quantity) }
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{productId}")
    fun removeItem(
        @LoginMember user: User,
        @PathVariable productId: Long
    ): ResponseEntity<Void> {
        cartService.removeItem(user.id!!, productId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{productId}")
    fun updateQuantity(
        @LoginMember user: User,
        @PathVariable productId: Long,
        @RequestBody request: UpdateQuantityRequest
    ): ResponseEntity<Void> {
        cartService.updateQuantity(user.id!!, productId, request.quantity)

        return if (request.quantity == 0) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok().build()
        }
    }

}
