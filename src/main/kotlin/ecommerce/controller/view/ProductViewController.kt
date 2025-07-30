package ecommerce.controller.view

import ecommerce.domain.Product
import ecommerce.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ProductViewController(private val productService: ProductService) {
    @GetMapping("/")
    fun welcomePage(): String {
        return "redirect:/admin/products"
    }

    @GetMapping("/admin/products")
    fun listAllProducts(model: Model): String {
        model.addAttribute("products", productService.getAllProducts())
        model.addAttribute("product", Product(0, "", 0.0, "", 0))
        return "products"
    }

    @GetMapping("/admin/products/edit-product/{id}")
    fun showEditProductForm(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val existingProduct = productService.getProductById(id)
        model.addAttribute("product", existingProduct)
        return "editProduct"
    }

    @PostMapping("/admin/products/edit-product/{id}")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @ModelAttribute("product") product: Product,
        model: Model,
    ): String {
        product.id = id
        try {
            productService.updateProduct(id, product)
        } catch (ex: Exception) {
            ex.printStackTrace()
            model.addAttribute("errorMessage", "Error updating the product ${product.id}")
            model.addAttribute("product", product)
        }
        return "redirect:/admin/products"
    }

    @GetMapping("/admin/products/add-product")
    fun showAddProduct(model: Model): String {
        model.addAttribute("product", Product(0, "", 0.0, "", 0))
        return "addProduct"
    }

    @PostMapping("/admin/products/add-product")
    fun addNewProduct(
        model: Model,
        @ModelAttribute("product") product: Product,
    ): String {
        try {
            productService.createProduct(product)
        } catch (ex: Exception) {
            ex.printStackTrace()
            model.addAttribute("errorMessage", "Error creating the product ${product.id}")
            model.addAttribute("product", product)
        }
        return "redirect:/admin/products"
    }

    @DeleteMapping("/admin/products/delete-product/{id}")
    fun deleteProduct(
        @PathVariable("id") id: Long,
    ): String {
        productService.deleteProduct(id)
        return "redirect:/admin/products"
    }
}
