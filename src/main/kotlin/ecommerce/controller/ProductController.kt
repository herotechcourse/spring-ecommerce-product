package ecommerce.controller

import ecommerce.dto.ProductDTO
import ecommerce.service.ProductService
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
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {
    @GetMapping
    fun getProducts(): ResponseEntity<List<ProductDTO>> = productService.getAllProducts()

    @GetMapping("/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<ProductDTO> = productService.getProductById(id)

    @PostMapping("")
    fun create(
        @RequestBody product: ProductDTO,
    ): ResponseEntity<Void> = productService.createProduct(product)

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: ProductDTO,
    ): ResponseEntity<Void> = productService.updateProduct(id, newProduct)

    @PatchMapping("/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: ProductDTO,
    ): ResponseEntity<Void> = productService.updateProduct(id, newProduct)

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> = productService.deleteProduct(id)
}
