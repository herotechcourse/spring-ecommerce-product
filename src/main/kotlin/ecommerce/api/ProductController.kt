package ecommerce.api

import ecommerce.ProductForm
import ecommerce.model.Product
import ecommerce.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ProductController(private val productService: ProductService) {
    @PostMapping("/products")
    fun createProduct(
        @RequestBody @Valid productForm: ProductForm,
        bindingResult: BindingResult,
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val errors =
                bindingResult.fieldErrors.associate { error ->
                    error.field to (error.defaultMessage ?: "Invalid value")
                }
            return ResponseEntity.badRequest().body(errors)
        }
        return productService.insert(productForm)
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
    ): ResponseEntity<Product> {
        val product = Product.toEntity(newProduct, id)
        val result = productService.update(product)
        if (result == 1) {
            val target = productService.findById(id) ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok(target)
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productService.findById(id) ?: return ResponseEntity.notFound().build()
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
