package ecommerce.controller

import ecommerce.exception.NotFoundException
import ecommerce.model.Product
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
import java.util.concurrent.atomic.AtomicLong

@Controller
@RequestMapping("/products")
class ProductController {
    private val index = AtomicLong(1)
    private val products: MutableList<Product> = mutableListOf()

    @PostMapping("")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val newProduct = Product.toEntity(product, index.getAndIncrement())
        products.add(newProduct)
        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(newProduct)
    }

    @GetMapping("/new")
    fun showCreateForm(): String {
        return "create_product_form"
    }

    @GetMapping("")
    fun read(model: Model): String {
        model.addAttribute("products", products)
        return "products"
    }

    @GetMapping("/edit/{id}")
    fun showUpdateForm(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val product = findProduct(id)
        model.addAttribute("product", product)
        return "edit_product_form"
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        val product = findProduct(id)
        product.update(newProduct)
        return ResponseEntity.ok(product)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        val product = findProduct(id)
        products.remove(product)
        return ResponseEntity.noContent().build()
    }

    private fun findProduct(id: Long): Product {
        return products.firstOrNull { it.id == id } ?: throw NotFoundException("Product with id $id not found")
    }
}
