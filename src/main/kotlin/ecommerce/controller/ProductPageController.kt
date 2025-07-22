package ecommerce.controller

import ecommerce.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductPageController(private val productService: ProductService) {
    @GetMapping("/products")
    fun getProducts(model: Model): String {
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "products"
    }
}
