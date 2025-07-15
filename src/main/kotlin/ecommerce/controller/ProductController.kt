package ecommerce.controller

import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI
import java.util.concurrent.atomic.AtomicLong

@Controller
class ProductController {
    private val index = AtomicLong(1)
    private val products: MutableList<Product> =
        (0..5).map { Product.toEntity(Product(name = "Name", price = 10.0, imageUrl = ""), index.getAndIncrement()) }.toMutableList()

    @PostMapping("/products")
    fun create(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        val newProduct = Product.toEntity(product, index.getAndIncrement())
        products.add(newProduct)
        return ResponseEntity.created(URI.create("/products/" + newProduct.id)).body(newProduct)
    }

    @GetMapping("/products")
    fun read(
        model: Model,
    ): String {
        model.addAttribute("products", products)
        return "products"
    }

    @GetMapping("/products/{id}")
    fun read(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Product> {
        return ResponseEntity.ok(findProduct(id))
    }

//    @GetMapping("/products")
//    fun temp(
//        model: Model,
//    ): String {
//        model.addAttribute("products", products)
//        return "products"
//    }

    @PutMapping("/products/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        val product = findProduct(id)
        product.update(newProduct)
        return ResponseEntity.ok(product)
    }

    @DeleteMapping("/products/{id}")
    fun delete(
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        val product = findProduct(id)
        products.remove(product)
        return ResponseEntity.noContent().build()
    }


    private fun findProduct(id: Long): Product {
        return products.firstOrNull { it.id == id } ?: throw NotFoundException("Product with id $id not found")
    }
}
