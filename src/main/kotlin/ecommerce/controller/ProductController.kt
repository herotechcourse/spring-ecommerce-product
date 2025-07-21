package ecommerce.controller

import ecommerce.ProductStore
import ecommerce.model.Product
import org.springframework.beans.factory.annotation.Qualifier
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
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URI

@Controller
@RequestMapping("/products")
class ProductController(
    @Qualifier("jdbcProductStore")
    private val productStore: ProductStore,
) {
    @PostMapping("")
    @ResponseBody
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Void> {
        val id = productStore.save(product)
        return ResponseEntity.created(URI.create("/products/$id")).build()
    }

    @GetMapping("")
    fun read(model: Model): String {
        model.addAttribute("products", productStore.findAll())
        return "products"
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Void> {
        productStore.update(id, newProduct)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun delete(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        productStore.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/new")
    fun showCreateForm(): String {
        return "create_product_form"
    }

    @GetMapping("/edit/{id}")
    fun showUpdateForm(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val product = productStore.findById(id)
        model.addAttribute("product", product)
        return "edit_product_form"
    }
}
