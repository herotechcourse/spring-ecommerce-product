package ecommerce.controller

import ecommerce.model.Product
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.HashMap

@Controller
@RequestMapping
class ProductController {
    private val products: MutableMap<Long, Product> = HashMap()

    @PostMapping("/product", consumes = ["application/json"])
    @ResponseBody
    fun addProduct(
        @RequestBody product: Product,
    ): Product {
        val product = Product(product.name, product.price, product.imageUrl)
        products.put(
            key = products.size.toLong() + 1,
            value = product,
        )
        return product
    }
}
