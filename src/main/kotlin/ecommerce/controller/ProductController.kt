package ecommerce.controller

import ecommerce.model.Product
import ecommerce.dto.ProductDTO
import ecommerce.service.ProductService
import ecommerce.repository.ProductStore
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/products")
@RestController
class ProductController(private val repository: ProductStore, private val productService: ProductService) {

    @PostMapping
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val newProduct = Product.Companion.toEntity(product)

        productService.validateName(newProduct.name)
        productService.validatePrice(newProduct.price)
        productService.validateUrl(newProduct.imageUrl)

        val savedProduct = repository.insert(newProduct)

        return ResponseEntity.created(URI.create("/products/" + savedProduct.id)).body(savedProduct)
    }

    @GetMapping
    fun read(): ResponseEntity<List<Product>> {
        if (repository.isEmptyOrNull()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(repository.findAll())
    }

    @PatchMapping("/{id}")
    fun patchUpdate(
        @RequestBody dto: ProductDTO,
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        dto.validate().name
        dto.validate().price
        dto.validate().imageUrl

        val existingProduct = repository[id] ?: return ResponseEntity.notFound().build()
        val updatedProduct = existingProduct.updateWith(dto)
        repository.updateById(id, updatedProduct)

        return ResponseEntity.ok().body(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        repository.deleteById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }
}
