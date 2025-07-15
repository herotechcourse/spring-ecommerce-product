package ecommerce.api

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

    @PostMapping("/api/products")
    fun createProduct(@RequestBody product: Product): ResponseEntity<Void> {
        val id = index.getAndIncrement()
        val newProduct = Product.toEntity(product, id)
        products.put(id, newProduct)
        return ResponseEntity.created(URI("/api/products/${newProduct.id}")).build()
    }
}