package ecommerce.controller

import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.concurrent.atomic.AtomicLong

@Controller
@RequestMapping
class ProductController {
    private val products: MutableSet<Product> = HashSet()

    private var index: AtomicLong = AtomicLong(1)

    @PostMapping("/product", consumes = ["application/json"])
    @ResponseBody
    fun addProduct(
        @RequestBody product: Product,
    ): Product {
        val product = Product(index.getAndAdd(1), product.name, product.price, product.imageUrl)
        products.add(product)
        return product
    }

    @PostMapping("/product2", consumes = ["application/json"])
    fun addProductResponseEntity(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val product = Product(product.id, product.name, product.price, product.imageUrl)
        products.add(product)
        return ResponseEntity.ok(product)
    }

    @GetMapping("/product/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product =
            products.first { it.id == id }
        return ResponseEntity.ok(product)
    }
}
