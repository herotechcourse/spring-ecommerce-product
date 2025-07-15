package ecommerce.controller

import ecommerce.Product
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
    private val index: AtomicLong = AtomicLong(1)

    @PostMapping("/products")
    fun createProduct(
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        val product: Product = Product.create(newProduct, index.andIncrement)
        products.put(newProduct.id, product)
        return ResponseEntity.created(URI.create("/products/${newProduct.id}")).build()
    }

    @GetMapping("/products/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Product> {
        return ResponseEntity.ok().body(products[id])
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<MutableMap<Long, Product>> {
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val updatedProduct = products[id] ?: throw Exception("Product with id $id not found")
        updatedProduct.update(product)
        return ResponseEntity.ok().body(updatedProduct)
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        products.remove(id)
        return ResponseEntity.noContent().build()
    }
}
