package ecommerce

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.net.URI

@Controller
class ProductController(private val productRepository: ProductRepository) {
    //private val products: MutableMap<Long, Product> = HashMap()
    //private val index = AtomicLong(0)

    @PostMapping("/products")
    @ResponseBody
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
//        val newProduct = Product.toEntity(product, index.incrementAndGet())
//        val productId = newProduct.id ?: throw RuntimeException("Product id is null")
//        products.putIfAbsent(productId, newProduct)
       // productRepository.insert(product)
        productRepository.insert(product)
        return ResponseEntity.created(URI.create("/products/${product.id}")).build()
    }

    @GetMapping("/products")
    @ResponseBody
    fun read(): ResponseEntity<List<Product>> {
        val products = productRepository.findAllProducts()
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/products/{id}")
    @ResponseBody
    fun update(
        @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.findProductById(id)?.update(newProduct) ?: create(newProduct) // TODO check this
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    @ResponseBody
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/admin/products")
    fun table(model: Model): String {
        model.addAttribute("products", productRepository.findAllProducts())
        model.addAttribute("product", Product())
        return "products"
    }
}

// TODO read about difference btw RestController and Controller
// TODO why do we combine Controller and ControllerView
