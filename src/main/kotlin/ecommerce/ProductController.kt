package ecommerce

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
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
}
