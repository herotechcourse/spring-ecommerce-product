package ecommerce

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
class ProductController {
    private val products: MutableMap<Long, Product> = HashMap()
    private val index = AtomicLong(1)

    @PostMapping("/products")
    fun create(@RequestBody product: Product): ResponseEntity<Void> {
        val newProduct = Product.toEntity(product, index.incrementAndGet())
        val productId = newProduct.id ?: throw RuntimeException("Product id is null")
        products.putIfAbsent(productId, newProduct)
        return ResponseEntity.created(URI.create("/products/${newProduct.id}")).build()
    }

    @GetMapping("/products")
    fun read(): ResponseEntity<MutableMap<Long, Product>> {
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/products/{id}")
    fun update(@RequestBody newProduct: Product, @PathVariable id: Long): ResponseEntity<Void> {
        if (!products.containsKey(id)) {
            create(newProduct)
            return ResponseEntity.ok().build()
        }
        val product = products[id] ?: throw RuntimeException("Product id is null")
        product.update(newProduct)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        products.remove(id) ?: throw RuntimeException("Product id is null")
        return ResponseEntity.noContent().build()
    }
}
