package ecommerce

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URI

@Controller
class ProductController(private val productRepository: ProductRepository) {
    @PostMapping("/products")
    @ResponseBody
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
        productRepository.insert(product)
        return ResponseEntity.created(URI.create("/products/${product.id}")).build()
    }

    @GetMapping("/products")
    @ResponseBody
    fun read(): ResponseEntity<List<Product>> {
        val products = productRepository.findAllProducts()
        return ResponseEntity.ok().body(products)
    }

    @PatchMapping("/products/{id}")
    @ResponseBody
    fun update(
        @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (!productRepository.update(newProduct, id))
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    @ResponseBody
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (!productRepository.delete(id))
            return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/admin/products")
    fun table(model: Model): String {
        model.addAttribute("products", productRepository.findAllProducts())
        model.addAttribute("product", Product())
        return "products"
    }
}
