package ecommerce.service

import ecommerce.ProductForm
import ecommerce.dao.JdbcProductDAO
import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URI

@Service
class ProductService(private val jdbcProductDao: JdbcProductDAO) {
    fun insert(form: ProductForm): ResponseEntity<Any> {
        if (jdbcProductDao.existsByName(form.name)) {
            val responseBody = mapOf("name" to "Product with name '${form.name}' already exists.")
            return ResponseEntity.badRequest().body(responseBody)
        }
        val product = ProductForm.toProduct(form)
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

    fun findAll(): List<Product> = jdbcProductDao.findAll()

    fun findById(id: Long): Product? = jdbcProductDao.findById(id)

    fun update(product: Product): Int = jdbcProductDao.update(product)

    fun delete(id: Long): Int = jdbcProductDao.delete(id)
}
