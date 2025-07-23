package ecommerce.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class ProductController(private val productRepository: ProductRepository) {
    @PostMapping("/products")
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        productRepository.insert(product)
        val uri = URI.create("/api/products/${product.id}")
        return ResponseEntity.created(uri).body(product)
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<Product>> {
        val products = productRepository.findAll()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/products/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(product)
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        val result = productRepository.update(id, newProduct)
        if (result == 1) {
            val product = productRepository.findById(id) ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok(product)
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        productRepository.delete(id)
        return ResponseEntity.noContent().build()
    }
}
