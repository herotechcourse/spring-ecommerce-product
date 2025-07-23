package ecommerce.controller.guest

import ecommerce.dto.products.ProductDTO
import ecommerce.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GuestProductController(
    private val productRepository: ProductRepository,
) {
    @GetMapping("/products")
    fun listProducts(): ResponseEntity<List<ProductDTO>> {
        val products = productRepository.findAll()
        return ResponseEntity.ok().body(products)
    }
}
