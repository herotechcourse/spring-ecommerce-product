package ecommerce.controller

import ecommerce.dto.ProductResponse
import ecommerce.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class GuestProductController(
    private val productService: ProductService,
) {
    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        return ResponseEntity.ok(productService.findAll())
    }

    @GetMapping("/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.findById(id))
    }
}
