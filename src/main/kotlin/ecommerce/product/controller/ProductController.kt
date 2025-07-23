package ecommerce.product.controller

import ecommerce.product.data.ProductMapper
import ecommerce.product.data.ProductRequest
import ecommerce.product.data.ProductResponse
import ecommerce.store.ProductStore
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProductController(private val store: ProductStore) {
    @PostMapping(PRODUCTS_ENDPOINT)
    fun createProduct(
        @RequestBody request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        return try {
            val createdProduct = store.create(request)
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductMapper.toResponse(createdProduct))
        } catch (exception: DataAccessException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping(PRODUCTS_ENDPOINT)
    fun getProducts(): ResponseEntity<List<ProductResponse>> {
        return ResponseEntity
            .ok(
                store.findAll()
                    .map {
                        ProductMapper.toResponse(it)
                    }
            )
    }

    @PutMapping(PRODUCT_BY_ID_ENDPOINT)
    fun updateProduct(
        @RequestBody request: ProductRequest,
        @PathVariable id: Long,
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = store.update(id, request)
        return when (updatedProduct) {
            null -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(ProductMapper.toResponse(updatedProduct))
        }
    }

    @DeleteMapping(PRODUCT_BY_ID_ENDPOINT)
    fun deleteProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (!store.deleteById(id)) return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }

    companion object {
        const val PRODUCTS_ENDPOINT = "/api/products"
        const val PRODUCT_BY_ID_ENDPOINT = "/api/products/{id}"
    }
}
