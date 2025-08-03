package ecommerce.controller

import ecommerce.annotation.AdminOnly
import ecommerce.annotation.LoginMember
import ecommerce.annotation.Protected
import ecommerce.dto.DeleteProductRequest
import ecommerce.dto.UpdateProductRequest
import ecommerce.model.Member
import ecommerce.model.Product
import ecommerce.model.Role
import ecommerce.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/products")
@RestController
class ProductController(private val productService: ProductService) {
    @PostMapping
    @Protected
    @AdminOnly
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val savedProduct = productService.createProduct(product)
        return ResponseEntity.created(URI.create("/products/${savedProduct.id}")).body(savedProduct)
    }

    @GetMapping
    fun read(): ResponseEntity<List<Product>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }

    @PatchMapping()
    @Protected
    fun patchUpdate(
        @RequestBody payload: UpdateProductRequest,
    ): ResponseEntity<Product> {
        val updatedProduct =
            productService.updateProduct(payload.productId, payload.product)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping()
    @Protected
    @AdminOnly
    fun delete(
        @RequestBody body: DeleteProductRequest,
        @LoginMember member: Member,
    ): ResponseEntity<Void> {
        if (member.role != Role.ADMIN) {
            return ResponseEntity.status(401).build()
        }
        val deleted = productService.deleteProductById(body.productId)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
