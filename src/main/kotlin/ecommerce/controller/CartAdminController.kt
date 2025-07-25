package ecommerce.controller

import ecommerce.service.CartService
import ecommerce.auth.annotation.LoginMember
import ecommerce.model.Member
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/stats")
class CartAdminController(
    private val cartService: CartService,
) {
    @GetMapping("/top-products")
    fun getTopProducts(
        @LoginMember member: Member
    ): ResponseEntity<Any> {
        val result = cartService.getTop5MostAddedProducts()
        return ResponseEntity.ok(result)
    }
}
