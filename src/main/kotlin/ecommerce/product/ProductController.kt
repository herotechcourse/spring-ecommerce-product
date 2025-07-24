package ecommerce.product

import ecommerce.repository.ProductRepository
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
import java.util.concurrent.atomic.AtomicLong

@RequestMapping("/api/products")
@RestController
class ProductController(private val repository: ProductRepository) {
    private val index = AtomicLong(1)

    @PostMapping("")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val id = index.getAndIncrement()
        val newProduct = Product.toEntity(product, id)
        repository.insert(id, newProduct)

        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(product)
    }

    @GetMapping("")
    fun read(): ResponseEntity<List<Product>> {
        if (repository.isEmptyOrNull()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(repository.findAll())
    }

    @PatchMapping("/{id}")
    fun patchUpdate(
        @RequestBody dto: ProductDTO,
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
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