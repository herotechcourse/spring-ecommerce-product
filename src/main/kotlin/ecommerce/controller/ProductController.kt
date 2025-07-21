package ecommerce.controller

import ecommerce.model.Product
import ecommerce.repository.ProductRepository
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
class ProductController {
    @GetMapping
    fun getProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok().body(ProductRepository().findAll())
    }

    @GetMapping("/{id}")
    fun getProductById(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Product> {
        return ResponseEntity.ok().body(ProductRepository().findById(id))
    }

    @PostMapping("")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
        ProductRepository().save(product)
        return ResponseEntity.created(URI.create("/products/")).build()
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Void> {
        ProductRepository().update(id, newProduct)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        ProductRepository().deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
