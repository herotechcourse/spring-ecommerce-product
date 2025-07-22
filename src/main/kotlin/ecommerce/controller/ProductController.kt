package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.repository.ProductRepository
import ecommerce.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productRepository: ProductRepository,
    private val productService: ProductService,
) {
    @PostMapping
    fun createProduct(
        @RequestBody product: ProductRequest,
    ): ResponseEntity<Void> {
        productService.createProduct(product)
        return ResponseEntity(HttpStatus.CREATED)
    }

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

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody product: ProductRequest,
    ): ResponseEntity<Void> {
        productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        return when (productRepository.updateProduct(id, product)) {
            true -> ResponseEntity(HttpStatus.OK)
            false -> ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.findById(id) ?: return ResponseEntity.notFound().build()
        return when (productRepository.deleteProduct(id)) {
            true -> ResponseEntity(HttpStatus.NO_CONTENT)
            false -> ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}
