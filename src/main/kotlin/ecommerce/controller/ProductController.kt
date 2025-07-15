package ecommerce.controller

import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.HashMap
import java.util.concurrent.atomic.AtomicLong

@Controller
@RequestMapping
class ProductController {
    private val products: MutableMap<Long, Product> = HashMap()
    private var index: AtomicLong = AtomicLong(0)

    @PostMapping("/product", consumes = ["application/json"])
    @ResponseBody
    fun addProduct(
        @RequestBody product: Product,
    ): Product {
        val product = Product(index.getAndAdd(1), product.name, product.price, product.imageUrl)
        products.put(
            key = products.size.toLong() + 1,
            value = product,
        )
        return product
    }

    @PostMapping("/product2", consumes = ["application/json"])
    fun addProductResponseEntity(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val product = Product(product.id, product.name, product.price, product.imageUrl)
        products.put(
            key = 1,
            value = product,
        )
        return ResponseEntity.ok(product)
    }
}
