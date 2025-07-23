package ecommerce.controller

import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductRestController(
    private val productRepository: ProductRepository,
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(productRepository.getAll())
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(product)
    }

    @PostMapping
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        productRepository.createProduct(product)
        val saved = productRepository.getAll().last()
        return ResponseEntity.ok(saved)
    }

    @PutMapping
    fun update(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        productRepository.updateProduct(product)
        return ResponseEntity.ok(product)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        productRepository.deleteProduct(id)
        return ResponseEntity.ok().build()
    }
}
