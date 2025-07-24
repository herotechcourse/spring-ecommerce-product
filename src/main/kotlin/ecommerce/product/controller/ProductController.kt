package ecommerce.product.controller

import ecommerce.product.data.ProductMapper
import ecommerce.product.data.ProductRequest
import ecommerce.product.data.ProductResponse
import ecommerce.product.store.ProductStore
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
class ProductController(private val store: ProductStore) {
    @PostMapping(PRODUCTS_ENDPOINT)
    fun createProduct(
        @Valid @RequestBody request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        return try {
            val id = store.insertWithKeyholder(request)
            val product =
                requireNotNull(store.findById(id)) {
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
            store.findAll()
                .map { ProductMapper.toResponse(it) },
        )
    }

    @PutMapping(PRODUCT_BY_ID_ENDPOINT)
    fun updateProduct(
        @Valid @RequestBody request: ProductRequest,
        @PathVariable id: Long,
    ): ResponseEntity<ProductResponse> {
        return try {
            val updatedProduct = store.putById(id, request)
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
            store.deleteById(id)
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
