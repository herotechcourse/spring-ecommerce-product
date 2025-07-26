package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.dto.mapper.ProductMapper
import ecommerce.repository.ProductRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val repository: ProductRepository) {
    @PostMapping(PRODUCTS_ENDPOINT)
    fun createProduct(
        @Valid @RequestBody request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        return try {
            val id = repository.insertWithKeyholder(request)
            val product =
                requireNotNull(repository.findById(id)) {
                    "Product with id $id could not be found"
                }

            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductMapper.toResponse(product))
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping(PRODUCTS_ENDPOINT)
    fun getProducts(): ResponseEntity<List<ProductResponse>> {
        return ResponseEntity.ok(
            repository.findAll()
                .map { ProductMapper.toResponse(it) },
        )
    }

    @PutMapping(PRODUCT_BY_ID_ENDPOINT)
    fun updateProduct(
        @Valid @RequestBody request: ProductRequest,
        @PathVariable id: Long,
    ): ResponseEntity<ProductResponse> {
        return try {
            val updatedProduct = repository.putById(id, request)
            ResponseEntity.ok(ProductMapper.toResponse(updatedProduct))
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping(PRODUCT_BY_ID_ENDPOINT)
    fun deleteProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        return try {
            repository.deleteById(id)
            ResponseEntity.noContent().build()
        } catch (exception: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    companion object {
        const val PRODUCTS_ENDPOINT = "/api/products"
        const val PRODUCT_BY_ID_ENDPOINT = "/api/products/{id}"
    }
}
