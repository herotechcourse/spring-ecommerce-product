package ecommerce.controller

import ecommerce.dto.product.ProductPatchRequest
import ecommerce.dto.product.ProductRequest
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import jakarta.validation.Valid
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
    ): Product = productRepository.findById(id)

    @PostMapping()
    fun createProduct(
        @Valid @RequestBody productRequest: ProductRequest,
    ): ResponseEntity<Product> {
        val product =
            Product(
                name = productRequest.name,
                price = productRequest.price,
                imageUrl = productRequest.imageUrl,
            )
        val saved = productRepository.save(product)
        return ResponseEntity.created(URI.create("/api/products/${saved.id}")).body(saved)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @Valid @RequestBody productRequest: ProductRequest,
        @PathVariable id: Long,
    ): Product {
        val product =
            Product(
                id = id,
                name = productRequest.name,
                price = productRequest.price,
                imageUrl = productRequest.imageUrl,
            )
        return productRepository.update(id, product)
    }

    @PatchMapping("/{id}")
    fun updateProductPartially(
        @Valid @RequestBody productPatchRequest: ProductPatchRequest,
        @PathVariable id: Long,
    ): Product {
        return productRepository.patch(id, productPatchRequest)
    }

    @DeleteMapping("/{id}")
    fun deleteProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        productRepository.delete(id)
        return ResponseEntity.noContent().build()
    }
}
