package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.model.toDto
import ecommerce.repository.ProductRepository
import ecommerce.repository.ProductResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(private val productRepository: ProductRepository) {
    @PostMapping(consumes = ["application/json"])
    @ResponseBody
    fun createProduct(
        @RequestBody product: ProductRequest,
    ): ResponseEntity<Void> {
        productRepository.createProduct(product)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(product.toDto())
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody product: ProductRequest,
    ): ResponseEntity<Void> {
        val oldProduct = productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        productRepository.updateProduct(id, product)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.deleteProduct(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
