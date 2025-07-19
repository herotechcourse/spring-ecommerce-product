package ecommerce.product

import ecommerce.store.ProductStore
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@Controller
class ProductController(
    private val repository: ProductStore,
    private val productService: ProductService
) {

    private val index = AtomicLong(1)

    @PostMapping("/api/products")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val id = index.getAndIncrement()
        val newProduct = Product.toEntity(product, id)
        productService.validateName(newProduct.name)
        productService.validatePrice(product.price)
        productService.validateUrl(product.imageUrl)

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
        val existingProduct = repository[id] ?: return ResponseEntity.notFound().build()
        productService.validateName(newProduct.name)
        productService.validatePrice(newProduct.price)
        productService.validateUrl(newProduct.imageUrl)

        existingProduct.update(newProduct)
        repository.updateById(id, existingProduct)
        return ResponseEntity.ok().body(existingProduct)
    }

    @DeleteMapping("/api/products/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        repository.deleteById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }
}
