package ecommerce.controller

import ecommerce.dto.CartItemRequest
import ecommerce.model.Member
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/wishes")
class CartController {
    @PostMapping
    fun addToCart(
        @RequestBody request: CartItemRequest,
        member: Member,
    ) {
        // Add product to cart for this member
    }
}
