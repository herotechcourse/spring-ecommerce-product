package ecommerce.api

import ecommerce.dao.JdbcProductDAO
import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class ProductController(private val jdbcProductDao: JdbcProductDAO) {
    @PostMapping("/products")
    fun createProduct(
        @RequestBody product: Product,
    ): ResponseEntity<Product> {
        var id: Long
        try {
            id = jdbcProductDao.insert(product)
        } catch (exception: Exception) {
            println("createProduct(): " + exception.message)
            return ResponseEntity.internalServerError().build()
        }
        val insertedProduct = jdbcProductDao.findById(id)
        val uri = URI.create("/api/products/$id")
        return ResponseEntity.created(uri).body(insertedProduct)
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<Product>> {
        val products = jdbcProductDao.findAll()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/products/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Product> {
        val product = jdbcProductDao.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(product)
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody newProduct: Product,
    ): ResponseEntity<Product> {
        val product = Product.toEntity(newProduct, id)
        val result = jdbcProductDao.update(product)
        if (result == 1) {
            val product = jdbcProductDao.findById(id) ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok(product)
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        jdbcProductDao.findById(id) ?: return ResponseEntity.notFound().build()
        jdbcProductDao.delete(id)
        return ResponseEntity.noContent().build()
    }
}
