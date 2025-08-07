package ecommerce.controller.view

import ecommerce.dto.ProductDTO
import ecommerce.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/shop")
class ProductsShopViewController(private val productService: ProductService) {
    @GetMapping
    fun displayProducts(model: Model): String {
        val products = productService.findAll()
        model.addAttribute("products", products.map { ProductDTO.Companion.from(it) })
        return "productsShop"
    }
}
