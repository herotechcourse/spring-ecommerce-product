package ecommerce.controller

import ecommerce.model.Product
import ecommerce.dto.ProductDTO
import ecommerce.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/products")
@RestController
class ProductController(private val productService: ProductService) {

    @PostMapping
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val savedProduct = productService.createProduct(product)
        return ResponseEntity.created(URI.create("/products/${savedProduct.id}")).body(savedProduct)
    }

    @GetMapping
    fun read(): ResponseEntity<List<Product>> {
        val products = productService.getAllProducts()
        return if (products.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(products)
    }

    @PatchMapping("/{id}")
    fun patchUpdate(
        @RequestBody dto: ProductDTO,
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(id, dto)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        val deleted = productService.deleteProductById(id)
        return if (deleted) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}
