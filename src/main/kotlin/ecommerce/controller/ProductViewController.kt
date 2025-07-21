package ecommerce.controller

import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/products")
class ProductViewController {
    @GetMapping("")
    fun read(model: Model): String {
        model.addAttribute("products", ProductRepository().findAll())
        return "products"
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
        val product = ProductRepository().findById(id)
        model.addAttribute("product", product)
        return "edit_product_form"
    }
}
