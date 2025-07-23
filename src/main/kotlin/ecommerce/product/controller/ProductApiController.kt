package ecommerce.product.controller

import ecommerce.product.domain.Product
import ecommerce.product.repository.ProductRepository
import jakarta.validation.Valid
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ProductApiController(private val productRepository: ProductRepository) {
    @PostMapping("/products")
    fun create(
        @Valid @RequestBody product: Product,
    ): ResponseEntity<Void> {
        productRepository.insert(product)
        return ResponseEntity.created(URI.create("/products/${product.id}")).build()
    }

    @GetMapping("/products")
    fun read(): ResponseEntity<List<Product>> {
        val products = productRepository.findAllProducts()
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/products/{id}")
    fun update(
        @Valid @RequestBody newProduct: Product,
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.edit(newProduct, id)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/products/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        productRepository.delete(id)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Invalid value"
            errors[fieldName] = errorMessage
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to (ex.message ?: "Validation error")), HttpStatus.BAD_REQUEST)
    }
}