package ecommerce.controller

import ecommerce.dto.CreateProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.dto.UpdateProductRequest
import ecommerce.service.ProductService
import ecommerce.utils.toModel
import ecommerce.utils.toResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ProductController(private val productService: ProductService) {
    @PostMapping("/products")
    fun createProduct(
        @Valid @RequestBody newProduct: CreateProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = newProduct.toModel()
        val createdProduct = productService.createProduct(product)
        return ResponseEntity.created(URI.create("/products/${createdProduct?.id}")).build()
    }

    @GetMapping("/products/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product.toResponse())
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts().map { it.toResponse() }
        return ResponseEntity.ok(products)
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: UpdateProductRequest,
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = request.toModel(id)
        productService.updateProduct(id, updatedProduct)
        return ResponseEntity.ok().body(updatedProduct.toResponse())
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
