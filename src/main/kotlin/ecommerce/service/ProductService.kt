package ecommerce.service

import ecommerce.controller.api.ProductController.Companion.MESSAGE_PRODUCT_NOT_FOUND
import ecommerce.controller.api.ProductController.Companion.MESSAGE_UNEXPECTED_PRODUCT_ACTION
import ecommerce.dao.JdbcProductDao
import ecommerce.dto.ProductForm
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import ecommerce.exception.ProductNameAlreadyExistsException
import ecommerce.model.Product
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<Product> {
        val originalProduct =
            jdbcProductDao.findById(id)
                ?: throw InternalServerErrorException("ProductService.update() - Product with ID $id not found")
        val originalName = originalProduct.name
        checkProductNameExists(form.name, originalName)
        val product = ProductForm.toEntity(form, id)
        val affectedRows = jdbcProductDao.update(product)
        when (affectedRows) {
            1 -> {
                val target =
                    jdbcProductDao.findById(id)
                        ?: throw InternalServerErrorException(MESSAGE_PRODUCT_NOT_FOUND)
                return ResponseEntity.ok(target)
            }
            0 -> throw NotFoundException(MESSAGE_PRODUCT_NOT_FOUND)
            else -> throw InternalServerErrorException(MESSAGE_UNEXPECTED_PRODUCT_ACTION)
        }
    }

    fun delete(id: Long): ResponseEntity<Void> {
        findById(id) ?: throw NotFoundException("Product not found - ID: $id")
        val affectedRows = jdbcProductDao.delete(id)
        when (affectedRows) {
            1 -> return ResponseEntity.noContent().build()
            0 -> throw NotFoundException("Product not found - ID: $id")
            else -> throw InternalServerErrorException(MESSAGE_UNEXPECTED_PRODUCT_ACTION)
        }
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
