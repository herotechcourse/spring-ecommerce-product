package ecommerce.controller.product

import ecommerce.dto.product.ProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.service.ProductService
import ecommerce.utils.toModel
import ecommerce.utils.toResponse
import jakarta.validation.Valid
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {
    @PostMapping("")
    fun createProduct(
        @Valid @RequestBody newProduct: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = newProduct.toModel(0)
        val createdProduct = productService.createProduct(product)
        return ResponseEntity.created(URI.create("/products/${createdProduct?.id}")).build()
    }

    @GetMapping("/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product.toResponse())
    }

    @GetMapping("")
    fun getProducts(): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts().map { it.toResponse() }
        return ResponseEntity.ok(products)
    }

    @PutMapping("{id}")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @Valid @RequestBody request: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = request.toModel(id)
        productService.updateProduct(id, updatedProduct)
        return ResponseEntity.ok().body(updatedProduct.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
