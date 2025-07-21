package ecommerce.controller.view

import ecommerce.model.ProductDTO
import ecommerce.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ProductViewController(private val productService: ProductService) {
    @GetMapping("/")
    fun displayProducts(model: Model): String {
        val products = productService.findAll()
        model.addAttribute("products", products.map { ProductDTO.from(it) })
        return "products"
    }

    @GetMapping("/add/product")
    fun displayCreateProductForm(): String {
        return "createProductForm"
    }

    @GetMapping("/update/product/{id}")
    fun displayUpdateProductForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val product = productService.findById(id)
        if (product != null) {
            model.addAttribute("product", ProductDTO.from(product))
        }
        return "updateProductForm"
    }
}
