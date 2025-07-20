package ecommerce.product

import ecommerce.repository.ProductStore
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@RestController
class ProductAPI(private val store: ProductStore) {
    private val index = AtomicLong(0)

    @PostMapping("/api/products")
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val id = index.getAndIncrement()
        val newProduct = Product.withId(product, id)

        store.save(id, newProduct)
        return ResponseEntity.created(URI.create("/products/$id")).body(product)
    }

    @GetMapping("/api/products")
    fun getProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(store.findAll())
    }

    @PutMapping("/api/products/{id}")
    fun updateProduct(
        @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = store.findById(id) ?: return ResponseEntity.notFound().build()
        product.update(newProduct)
        store.update(id, product)
        return ResponseEntity.ok().body(product)
    }

    @DeleteMapping("/api/products/{id}")
    fun deleteProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        store.deleteById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }
}
