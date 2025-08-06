package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.dto.mapper.ProductMapper.toNewProduct
import ecommerce.dto.mapper.ProductMapper.toResponse
import ecommerce.service.ProductService
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
class ProductController(private val service: ProductService) {
    @PostMapping(PRODUCTS_ENDPOINT)
    fun createProduct(
        @Valid @RequestBody request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = service.insertNewProduct(toNewProduct(request))

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(product))
    }

    @GetMapping(PRODUCTS_ENDPOINT)
    fun getProducts(): List<ProductResponse> = service.getAllProducts()

    @PutMapping(PRODUCT_BY_ID_ENDPOINT)
    fun updateProduct(
        @Valid @RequestBody request: ProductRequest,
        @PathVariable id: Long,
    ): ProductResponse {
        return toResponse(service.updateProduct(id, toNewProduct(request)))
    }

    @DeleteMapping(PRODUCT_BY_ID_ENDPOINT)
    fun deleteProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        service.deleteProductById(id)
        return ResponseEntity.noContent().build()
    }

    companion object {
        const val PRODUCTS_ENDPOINT = "/api/products"
        const val PRODUCT_BY_ID_ENDPOINT = "/api/products/{id}"
    }
}
