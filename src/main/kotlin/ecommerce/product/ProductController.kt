package ecommerce.product

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@Controller
class ProductController {
    private val products: MutableList<Product> = ArrayList()
    private val index = AtomicLong(1)

    @PostMapping("/products")
    fun create(@RequestBody product: Product): ResponseEntity<Product> {
       val newProduct = Product.toEntity(product, index.getAndIncrement())
        products.add(newProduct)
        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(product)
    }

    @GetMapping("/products")
    fun read(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/api/products/{id}")
    fun update(@RequestBody newProduct: Product, @PathVariable id: Long): ResponseEntity<Product> {
        val product = products.stream()
            .filter { it.id == id }
            .findFirst()
            .orElseThrow { RuntimeException("Product with id $id not found") }
        product.update(newProduct)
        return ResponseEntity.ok().body(product)
    }

}
