package ecommerce.controller

import ecommerce.dto.TopProductStatResponse
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    private val cartService: CartService,
) {
    @GetMapping("/topproducts")
    fun findTop5ProductsInLast30Days(): ResponseEntity<List<TopProductStatResponse>> {
        println("I was here")
        val topProducts = cartService.findTop5ProductsInLast30Days()

        return ResponseEntity.ok(topProducts)
    }
}
