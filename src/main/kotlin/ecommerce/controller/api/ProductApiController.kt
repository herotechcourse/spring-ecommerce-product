package ecommerce.controller.api

import ecommerce.annotation.LoginMember
import ecommerce.domain.Member
import ecommerce.dto.product.CreateProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.dto.product.UpdateProductRequest
import ecommerce.service.ProductService
import ecommerce.utils.toDomain
import ecommerce.utils.toResponse
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
import java.net.URI

@RestController
@RequestMapping("/api/products")
class ProductApiController(private val productService: ProductService) {
    @PostMapping
    fun createProduct(
        //@LoginMember member: Member,
        @Valid @RequestBody newProductRequest: CreateProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = newProductRequest.toDomain()
        productService.createProduct(product)
        return ResponseEntity.created(URI.create("/products/${product.id}")).body(product.toResponse())
    }

    @GetMapping("{id}")
    fun getProductById(
        @LoginMember member: Member,
        @PathVariable("id") id: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok().body(product.toResponse())
    }

    @GetMapping
    fun getProducts(@LoginMember member: Member,): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts().map { it.toResponse() }
        return ResponseEntity.ok().body(products)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @LoginMember member: Member,
        @Valid
        @PathVariable("id") id: Long,
        @RequestBody request: UpdateProductRequest,
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = request.toDomain(id)
        productService.updateProduct(id, updatedProduct)
        return ResponseEntity.ok().body(updatedProduct.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @LoginMember member: Member,
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
