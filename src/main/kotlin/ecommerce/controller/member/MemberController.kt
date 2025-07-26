package ecommerce.controller.member

import ecommerce.dto.product.ProductResponse
import ecommerce.service.ProductService
import ecommerce.utils.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController {
    @GetMapping("/products")
    fun getAllProducts(productService: ProductService): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts().map { it.toResponse() }
        return ResponseEntity.ok(products)
    }
}
