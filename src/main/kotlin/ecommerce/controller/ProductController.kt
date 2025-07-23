package ecommerce.controller

import ecommerce.dto.ProductDTO
import ecommerce.dto.ProductPatchDTO
import ecommerce.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {
    @GetMapping
    fun getProducts(): ResponseEntity<List<ProductDTO>> {
        return ResponseEntity.ok().body(productService.getAllProducts())
    }

    @GetMapping("/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<ProductDTO> {
        return ResponseEntity.ok().body(productService.getProductById(id))
    }

    @PostMapping("")
    fun create(
        @RequestBody @Valid product: ProductDTO,
    ): ResponseEntity<String> {
        val uri = productService.createProduct(product)
        return ResponseEntity.created(uri).body("Product created")
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody @Valid newProduct: ProductDTO,
    ): ResponseEntity<String> {
        productService.updateProduct(id, newProduct)
        return ResponseEntity.ok().body("Product updated")
    }

    @PatchMapping("/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @RequestBody @Valid patchProduct: ProductPatchDTO,
    ): ResponseEntity<String> {
        productService.patchProduct(id, patchProduct)
        return ResponseEntity.ok().body("Product updated")
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): ResponseEntity<String> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
