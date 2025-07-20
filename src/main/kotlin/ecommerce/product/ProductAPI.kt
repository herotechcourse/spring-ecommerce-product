package ecommerce.product

import ecommerce.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@RestController
class ProductAPI(private val repository: ProductStore) {
    private val index = AtomicLong(0)

    @PostMapping("/api/products")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val id = index.getAndIncrement()
        val newProduct = Product.toEntity(product, id)

        repository.insert(id, newProduct)
        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(product)
    }

    @GetMapping("/api/products")
    fun read(): ResponseEntity<List<Product>> {
        if (repository.isEmptyOrNull()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(repository.findAll())
    }

    @PutMapping("/api/products/{id}")
    fun update(
        @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = repository[id] ?: return ResponseEntity.notFound().build()
        product.update(newProduct)
        repository.updateById(id, product)
        return ResponseEntity.ok().body(product)
    }

    @DeleteMapping("/api/products/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        repository.deleteById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }
}
