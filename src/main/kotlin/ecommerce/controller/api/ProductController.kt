package ecommerce.controller.api

import ecommerce.model.Product
import ecommerce.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class ProductController(private val productService: ProductService) {

    @PostMapping("/products")
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
        productService.insert(product)
        return ResponseEntity.created(URI("/products/${product.id}")).build()
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<Product>> {
        val products = productService.findAll()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/products/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = productService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(product)
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Void> {
        val updated = productService.update(id, newProduct)
        if (updated) {
            return ResponseEntity.ok().build()
        }
        return    ResponseEntity.notFound().build()
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        val deleted = productService.delete(id)
        if (deleted) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.notFound().build()
    }
}
