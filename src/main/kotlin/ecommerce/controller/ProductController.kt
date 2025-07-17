package ecommerce.controller

import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
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
    @Autowired
    private lateinit var productRepository: ProductRepository

    private val products: MutableSet<Product> = HashSet()

    private var index: AtomicLong = AtomicLong(1)

    @PostMapping(consumes = ["application/json"])
    @ResponseBody
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        productRepository.createProduct(product)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getProducts(model: Model): String {
        model.addAttribute("products", productRepository.getAll())
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
    @ResponseBody
    fun updateProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        productRepository.updateProduct(product)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        productRepository.deleteProduct(id)
        return ResponseEntity.ok().build()
    }
}
