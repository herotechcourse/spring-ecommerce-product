package ecommerce.controller.api

import ecommerce.dto.ProductForm
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import ecommerce.exception.ProductNameAlreadyExistsException
import ecommerce.model.Product
import ecommerce.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {
    @PostMapping
    fun createProduct(
        @RequestBody @Valid productForm: ProductForm,
    ): ResponseEntity<Product> {
        val product = productService.insert(productForm)
        val uri = URI.create("/api/products/${product.id}")
        return ResponseEntity.created(uri).body(product)
    }

    @GetMapping
    fun getProducts(): ResponseEntity<List<Product>> {
        val products = productService.findAll()
        return ResponseEntity.ok(products)
    }

    @GetMapping("{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = productService.findById(id) ?: throw NotFoundException("Product not found - ID: $id")
        return ResponseEntity.ok(product)
    }

    @PutMapping("{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody @Valid productForm: ProductForm,
    ): ResponseEntity<Product> {
        val result = productService.update(productForm, id)
        when (result) {
            1 -> {
                val target =
                    productService.findById(id) ?: throw InternalServerErrorException("Product not found - ID: $id")
                return ResponseEntity.ok(target)
            }
            0 -> throw NotFoundException("Product not found - ID: $id")
            else -> throw InternalServerErrorException("Unexpected update on Product - ID: $id")
        }
    }

    @DeleteMapping("{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productService.findById(id) ?: throw NotFoundException("Product not found - ID: $id")
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(ProductNameAlreadyExistsException::class)
    fun handleProductNameAlreadyExistsExceptionHandler(e: Exception): ResponseEntity<Map<String, Any>> {
        val error = mapOf("name" to e.message)
        val errorBody = mapOf("errors" to error)
        println("ProductNameAlreadyExistsException occurred: $errorBody")
        return ResponseEntity.badRequest().body(errorBody)
    }
}
