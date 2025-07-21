package ecommerce

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URI

@Controller
class ProductController(private val productRepository: ProductRepository) {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleConstraintViolations(e: MethodArgumentNotValidException): ResponseEntity<String> {
        val errors = e.bindingResult.fieldErrors.joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(errors)
    }

    @PostMapping("/products")
    @ResponseBody
    fun create(
        @RequestBody @Valid product: Product,
    ): ResponseEntity<Unit> {
        val id = productRepository.insertWithKeyHolder(product)
        return ResponseEntity.created(URI.create("/products/$id")).build()
    }

    @GetMapping("/products")
    @ResponseBody
    fun read(): @Valid ResponseEntity<List<Product>> {
        val products = productRepository.findAllProducts()
        return ResponseEntity.ok().body(products)
    }

    @PatchMapping("/products/{id}")
    @ResponseBody
    fun update(
        @RequestBody @Valid newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        if (!productRepository.update(newProduct, id))
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    @ResponseBody
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
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
