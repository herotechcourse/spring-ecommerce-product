package ecommerce.controller

import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@Controller
class ProductController {
    private val products: MutableList<Product> = ArrayList()
    private val index = AtomicLong(1)

    @PostMapping("/products")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val newProduct = Product.toEntity(product, index.getAndIncrement())
        products.add(newProduct)
        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(newProduct)
    }

    @GetMapping("/products")
    fun read(
    ): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(products)
    }

    @GetMapping("/products/{id}")
    fun read(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Product> {
        return ResponseEntity.ok(findProduct(id))
    }

    @PutMapping("/products/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        val product = findProduct(id)
        product.update(newProduct)
        return ResponseEntity.ok(product)
    }

    private fun findProduct(id: Long): Product {
        return products.firstOrNull { it.id == id } ?: throw NotFoundException("Product with id $id not found")
    }
}
