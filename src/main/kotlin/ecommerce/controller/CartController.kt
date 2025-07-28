package ecommerce.controller

import ecommerce.common.LoginUser
import ecommerce.dto.CartItemRequest
import ecommerce.dto.CartItemResponse
import ecommerce.dto.UpdateQuantityRequest
import ecommerce.entity.User
import ecommerce.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @LoginUser user: User,
        @RequestBody request: CartItemRequest,
    ): ResponseEntity<Void> {
        cartService.addItem(user.id!!, request.productId, request.quantity) // <-- pass quantity here
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun getCart(
        @LoginUser user: User,
    ): ResponseEntity<List<CartItemResponse>> {
        val items = cartService.getCart(user.id!!)
        val response = items.map { CartItemResponse(it.productId, it.quantity) }
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{productId}")
    fun removeItem(
        @LoginUser user: User,
        @PathVariable productId: Long,
    ): ResponseEntity<Void> {
        cartService.removeItem(user.id!!, productId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{productId}")
    fun updateQuantity(
        @LoginUser user: User,
        @PathVariable productId: Long,
        @RequestBody request: UpdateQuantityRequest,
    ): ResponseEntity<Void> {
        cartService.updateQuantity(user.id!!, productId, request.quantity)

        return if (request.quantity == 0) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok().build()
        }
    }
}
