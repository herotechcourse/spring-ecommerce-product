package ecommerce.controller

import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URI

@Controller
@RequestMapping("/products")
class ProductController {
    @PostMapping("")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
        ProductRepository().save(product)
        return ResponseEntity.created(URI.create("/products/")).build()
    }

    @GetMapping("/new")
    fun showCreateForm(): String {
        return "create_product_form"
    }

    @GetMapping("")
    fun read(model: Model): String {
        model.addAttribute("products", ProductRepository().findAll())
        return "products"
    }

    @GetMapping("/edit/{id}")
    fun showUpdateForm(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val product = ProductRepository().findById(id)
        model.addAttribute("product", product)
        return "edit_product_form"
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
