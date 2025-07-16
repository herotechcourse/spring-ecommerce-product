package ecommerce

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@Controller
class ProductController {
    private val products: MutableMap<Long, Product> = HashMap()
    private val index = AtomicLong(0)

    @PostMapping("/products")
    @ResponseBody
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
        val newProduct = Product.toEntity(product, index.incrementAndGet())
        val productId = newProduct.id ?: throw RuntimeException("Product id is null")
        products.putIfAbsent(productId, newProduct)
        return ResponseEntity.created(URI.create("/products/${newProduct.id}")).build()
    }

    @GetMapping("/products")
    @ResponseBody
    fun read(): ResponseEntity<MutableMap<Long, Product>> {
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/products/{id}")
    @ResponseBody
    fun update(
        @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (!products.containsKey(id)) {
            create(newProduct)
            return ResponseEntity.ok().build()
        }
        val product = products[id] ?: throw RuntimeException("Product id is null")
        product.update(newProduct)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    @ResponseBody
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        products.remove(id) ?: throw RuntimeException("Product id is null")
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/admin/products")
    fun table(model: Model): String {
        model.addAttribute("products", products.values)
        model.addAttribute("product", Product())
        return "products"
    }
}

// TODO read about difference btw RestController and Controller
// TODO why do we combine Controller and ControllerView
