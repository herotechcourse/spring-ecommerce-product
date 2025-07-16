package ecommerce.product

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

@Controller
class ProductController {
    private val products: MutableMap<Long, Product> = HashMap()
    private val index = AtomicLong(1)

    @PostMapping("/api/products")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val id = index.getAndIncrement()
        val newProduct = Product.toEntity(product, id)

        products.put(id, newProduct)
        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(product)
    }

    @GetMapping("/api/products")
    fun read(): ResponseEntity<List<Product>> {
        if (products.isEmpty()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(products.values.toList())
    }

    @PutMapping("/api/products/{id}")
    fun update(
        @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        try {
            val product = products.getValue(id)
            product.update(newProduct)
            return ResponseEntity.ok().body(product)
        } catch (exception: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/api/products/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (products.isEmpty()) return ResponseEntity.notFound().build()
        try {
            products.getValue(id)
        } catch (exception: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
        products.remove(id)
        return ResponseEntity.noContent().build()
    }
}
