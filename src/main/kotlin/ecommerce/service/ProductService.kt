package ecommerce.service

import ecommerce.dao.JdbcProductDAO
import ecommerce.dto.ProductForm
import ecommerce.exception.ProductNameAlreadyExistsException
import ecommerce.model.Product
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URI

@Service
class ProductService(private val jdbcProductDao: JdbcProductDAO) {
    fun insert(form: ProductForm): ResponseEntity<Product> {
        checkProductNameExists(form.name)
        val product = ProductForm.toProduct(form)
        val id = jdbcProductDao.insert(product)
        val insertedProduct = jdbcProductDao.findById(id)
        val uri = URI.create("/api/products/$id")
        return ResponseEntity.created(uri).body(insertedProduct)
    }

    private fun checkProductNameExists(name: String): Boolean {
        when (jdbcProductDao.existsByName(name)) {
            true -> {
                val message = "Product with name '$name' already exists."
                throw ProductNameAlreadyExistsException(message)
            }
            false -> return true
        }
    }

    fun findAll(): List<Product> = jdbcProductDao.findAll()

    fun findById(id: Long): Product? = jdbcProductDao.findById(id)

    fun update(
        form: ProductForm,
        id: Long,
    ): Int {
        checkProductNameExists(form.name)
        val product = ProductForm.toEntity(form, id)
        return jdbcProductDao.update(product)
    }

    fun delete(id: Long): Int = jdbcProductDao.delete(id)
}
