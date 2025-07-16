package ecommerce.controller

import ecommerce.model.Product
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.concurrent.atomic.AtomicLong

@Controller
@RequestMapping("/products")
class ProductController {
    private val products: MutableSet<Product> = HashSet()

    private var index: AtomicLong = AtomicLong(1)

    @PostMapping(consumes = ["application/json"])
    fun addProductResponseEntity(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val product = Product(index.getAndAdd(1), product.name, product.price, product.imageUrl)
        products.add(product)
        return ResponseEntity.ok(product)
    }

    @GetMapping
    fun getProducts(model: Model): String {
        model.addAttribute("products", products)
        return "table"
    }

    @GetMapping("/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        return try {
            val found = products.any { it.id == id }
            val product = products.first { it.id == id }
            if (found) {
                ResponseEntity.ok(product)
            } else {
                throw IllegalArgumentException()
            }
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping
    fun updateProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        products.removeIf { it.id == product.id }
        val newProduct = Product(product.id, product.name, product.price, product.imageUrl)
        products.add(newProduct)
        return ResponseEntity.ok(newProduct)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteProduct(
        @PathVariable id: Long,
    ) {
        products.removeIf { it.id == id }
    }
}
