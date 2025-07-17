package ecommerce.controller

import ecommerce.model.Product
import ecommerce.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ProductController(private val productService: ProductService) {
    @ResponseBody
    @PostMapping("/products")
    fun createProduct(
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        productService.createProduct(newProduct)
        return ResponseEntity.created(URI.create("/products/${newProduct.id}")).build()
    }

    @GetMapping("/products/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Product> {
        try {
            productService.getProductById(id)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ResponseEntity.ok().body(productService.getProductById(id))
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok().body(productService.getAllProducts())
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        productService.updateProduct(id, product)
        return ResponseEntity.ok().body(product)
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build()
        } else {
            return ResponseEntity.notFound().build()
        }
    }
}
