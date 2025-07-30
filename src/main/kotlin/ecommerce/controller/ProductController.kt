package ecommerce.controller

import ecommerce.exception.ConflictException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ProductController(private val productRepository: ProductRepository) {
    @PostMapping("/products")
    fun create(
        @RequestBody @Valid product: Product,
    ): ResponseEntity<Unit> {
        if (productRepository.existsByName(product.name)) {
            throw ConflictException("Product with name ${product.name} already exists")
        }
        val id = productRepository.insertWithKeyHolder(product)
        return ResponseEntity.created(URI.create("/products/$id")).build()
    }

    @GetMapping("/products")
    fun read(): @Valid ResponseEntity<List<Product>> {
        val products = productRepository.findAllProducts()
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/products/{id}")
    fun update(
        @RequestBody @Valid newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        if (!productRepository.existsById(id)) {
            return create(newProduct)
        }
        if (!productRepository.update(newProduct, id)) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        if (!productRepository.delete(id)) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.noContent().build()
    }
}
