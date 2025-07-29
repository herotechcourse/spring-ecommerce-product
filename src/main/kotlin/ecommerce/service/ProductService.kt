package ecommerce.service

import ecommerce.dao.JdbcProductDao
import ecommerce.dto.ProductForm
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.ProductNameAlreadyExistsException
import ecommerce.model.Product
import org.springframework.stereotype.Service

@Service
class ProductService(private val jdbcProductDao: JdbcProductDao) {
    fun insert(form: ProductForm): Product {
        checkProductNameExists(form.name)
        val product = ProductForm.toProduct(form)
        val id = jdbcProductDao.insert(product)
        return jdbcProductDao.findById(id)
            ?: throw InternalServerErrorException("ProductService.insert() - Product with ID $id not found")
    }

    fun findAll(): List<Product> = jdbcProductDao.findAll()

    fun findById(id: Long): Product? = jdbcProductDao.findById(id)

    fun update(
        form: ProductForm,
        id: Long,
    ): Int {
        val originalProduct =
            jdbcProductDao.findById(id)
                ?: throw InternalServerErrorException("ProductService.update() - Product with ID $id not found")
        val originalName = originalProduct.name
        checkProductNameExists(form.name, originalName)
        val product = ProductForm.toEntity(form, id)
        val numberOfAffectedRow = jdbcProductDao.update(product)
        return numberOfAffectedRow
    }

    fun delete(id: Long): Int {
        val numberOfAffectedRow = jdbcProductDao.delete(id)
        return numberOfAffectedRow
    }

    private fun checkProductNameExists(
        name: String,
        originalName: String? = null,
    ) {
        if (originalName != null && name == originalName) {
            return
        } else if (jdbcProductDao.existsByName(name)) {
            val message = "Product with name '$name' already exists."
            throw ProductNameAlreadyExistsException(message)
        }
    }
}
