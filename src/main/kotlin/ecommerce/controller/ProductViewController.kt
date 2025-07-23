package ecommerce.controller

import ecommerce.model.Product
import ecommerce.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductViewController(private val productService: ProductService) {
    @GetMapping("/")
    fun welcomePage(): String {
        return "redirect:/admin/products"
    }

    @GetMapping("/admin/products")
    fun listAllProducts(model: Model): String {
        model.addAttribute("products", productService.getAllProducts())
        model.addAttribute("product", ::Product)
        return "products"
    }
}
