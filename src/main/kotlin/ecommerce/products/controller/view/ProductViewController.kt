package ecommerce.products.controller.view

import ecommerce.products.model.ProductDTO
import ecommerce.products.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class ProductViewController(private val productService: ProductService) {
    @GetMapping("/")
    fun displayProducts(model: Model): String {
        val products = productService.findAll()
        model.addAttribute("products", products.map { ProductDTO.from(it) })
        return "adminPanel"
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
