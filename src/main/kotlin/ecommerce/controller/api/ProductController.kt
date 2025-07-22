package ecommerce.controller.api

import ecommerce.model.Product
import ecommerce.model.ProductDTO
import ecommerce.model.ProductPatchDTO
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

@RestController
@RequestMapping("/api")
class ProductController(private val productService: ProductService) {
    @PostMapping("/products")
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<ProductDTO> {
        val created = productService.insert(product)
        val dto = ProductDTO.from(created)
        return ResponseEntity.created(URI("/products/${dto.id}")).body(dto)
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<ProductDTO>> {
        val products = productService.findAll()
        val dtos = products.map { ProductDTO.from(it) }
        return ResponseEntity.ok(dtos)
    }

    @GetMapping("/products/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<ProductDTO> {
        val product = productService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProductDTO.from(product))
    }

    @PatchMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody newProduct: ProductPatchDTO,
    ): ResponseEntity<Void> {
        val updated = productService.update(id, newProduct)
        if (updated) {
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        val deleted = productService.delete(id)
        if (deleted) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.notFound().build()
    }
}
