package ecommerce.controller

import ecommerce.model.Product
import ecommerce.model.ProductPatchRequest
import ecommerce.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/products")
@RestController
class ProductController(private val productRepository: ProductRepository) {
    @GetMapping()
    fun getProducts(): List<Product> = productRepository.findAll()

    @GetMapping("/{id}")
    fun getProductById(
        @PathVariable id: Long,
    ): Product = productRepository.findById(id) // Repository throws NotFoundException if not found

    @PostMapping()
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val saved = productRepository.save(product)
        return ResponseEntity.created(URI.create("/api/products/${saved.id}")).body(saved)
    }

    @PutMapping("/{id}")
    fun updateProductById(
        @RequestBody product: Product,
        @PathVariable id: Long,
    ): Product = productRepository.update(id, product)

    @PatchMapping("/{id}")
    fun patchProductById(
        @RequestBody patchRequest: ProductPatchRequest,
        @PathVariable id: Long,
    ): Product = productRepository.patch(id, patchRequest)

    @DeleteMapping("/{id}")
    fun deleteProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        productRepository.delete(id)
        return ResponseEntity.noContent().build()
    }

    // DISABLED - Too dangerous for production
    // @DeleteMapping()
    // fun deleteAllProducts(): ResponseEntity<Unit> {
    //     productRepository.deleteAll()
    //     return ResponseEntity.noContent().build()
    // }
}
