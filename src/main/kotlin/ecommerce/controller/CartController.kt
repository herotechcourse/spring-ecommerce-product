package ecommerce.controller

import ecommerce.dto.CartDto
import ecommerce.dto.CartItemDto
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
@RequestMapping("/api/cart")
class CartController {

    @GetMapping
    fun getItems(
        @RequestBody request: CartDto
    ): ResponseEntity<List<CartItemDto>> {
        // authenticate user -> happens automatically via Interception
        // get items from service
        // return items
        return ResponseEntity.status(HttpStatus.OK).body(emptyList())
    }

    @PostMapping("/{productId}")
    fun addItem(
        @RequestBody request: CartDto,
        @PathVariable productId: Long,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{productId}}")
    fun deleteItem(
        @RequestBody request: CartDto,
        @PathVariable productId: Long,
    ): ResponseEntity<Unit> {
        return ResponseEntity.noContent().build()
    }
}
