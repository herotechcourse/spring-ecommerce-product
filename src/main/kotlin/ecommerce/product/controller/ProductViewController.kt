package ecommerce.product.controller

import ecommerce.product.domain.Product
import ecommerce.product.repository.ProductRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductViewController(private val productRepository: ProductRepository) {
    @GetMapping("/admin/products")
    fun table(model: Model): String {
        model.addAttribute("products", productRepository.findAllProducts())
        model.addAttribute("product", Product())
        return "products"
    }
}
