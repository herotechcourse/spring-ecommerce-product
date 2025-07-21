package ecommerce.controller

import ecommerce.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/products")
class ProductsViewController(
    @Autowired val productRepository: ProductRepository,
) {
    @GetMapping
    fun getProducts(model: Model): String {
        model.addAttribute("products", productRepository.getAll())
        return "index"
    }
}
