package ecommerce.controller

import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductPageController(private val jdbcProductStore: ProductRepository) {
    @GetMapping("/products")
    fun getProducts(model: Model): String {
        model.addAttribute("products", jdbcProductStore.getAll())
        return "products"
    }
}
