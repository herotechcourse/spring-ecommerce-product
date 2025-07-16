package ecommerce

import ecommerce.api.ProductController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductViewController(
    private val controller: ProductController,
) {
    @GetMapping("/")
    fun displayProducts(model: Model): String {
        // TODO: Figure out how to deal with controller connection to get data
        val products = controller.products.values.toList()

        model.addAttribute("products", products)
        return "products"
    }

    @GetMapping("/add/product")
    fun displayCreateProductForm(): String {
        return "createProductForm"
    }
}
