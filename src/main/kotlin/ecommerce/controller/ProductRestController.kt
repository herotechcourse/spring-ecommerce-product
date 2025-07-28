package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.exception.ErrorResponse
import ecommerce.exception.FieldError
import ecommerce.mapper.toEntity
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductRestController(
    private val productRepository: ProductRepository,
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(productRepository.getAll())
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product =
            productRepository.findById(id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(product)
    }

    @PostMapping
    fun create(
        @RequestBody @Valid request: ProductRequest,
    ): ResponseEntity<Any> {
        if (productRepository.existsByName(request.name)) {
            val error =
                ErrorResponse(
                    message = "Validation failed",
                    errors = listOf(FieldError("name", "Product name must be unique")),
                )
            return ResponseEntity.badRequest().body(error)
        }

        val product = request.toEntity()
        productRepository.createProduct(product)

        // val saved = productRepository.getAll().last()
        return ResponseEntity.ok().body(product)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: ProductRequest,
    ): ResponseEntity<Any> {
        val existingProduct =
            productRepository.findById(id)
                ?: return ResponseEntity.notFound().build()

        if (productRepository.existsByNameExcludingId(request.name, id)) {
            val error =
                ErrorResponse(
                    message = "Validation failed",
                    errors =
                        listOf(
                            FieldError("name", "Product name must be unique"),
                        ),
                )
            return ResponseEntity.badRequest().body(error)
        }

        val updatedProduct =
            existingProduct.copy(
                name = request.name,
                price = request.price,
                imageUrl = request.imageUrl,
            )

        productRepository.updateProduct(updatedProduct)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
//        val existingProduct =
//            productRepository.findById(id)
//                ?: return ResponseEntity.notFound().build()

        productRepository.deleteProduct(id)
        return ResponseEntity.ok().build()
    }
}
