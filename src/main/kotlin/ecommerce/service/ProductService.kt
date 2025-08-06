package ecommerce.service

import ecommerce.dto.product.ProductPatchRequest
import ecommerce.dto.product.ProductRequest
import ecommerce.exception.ProductValidationException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    fun createProduct(request: ProductRequest): Product {
        val product =
            Product(
                name = request.name,
                price = request.price,
                imageUrl = request.imageUrl,
            )
        return try {
            productRepository.save(product)
        } catch (e: DataIntegrityViolationException) {
            throw ProductValidationException("Product name already exists")
        }
    }

    fun updateProduct(
        id: Long,
        request: ProductRequest,
    ): Product {
        val product =
            Product(
                id = id,
                name = request.name,
                price = request.price,
                imageUrl = request.imageUrl,
            )
        return try {
            productRepository.update(id, product)
        } catch (e: DataIntegrityViolationException) {
            if (e.message?.contains("name", ignoreCase = true) == true) {
                throw ProductValidationException("Product name already exists")
            }
            throw e
        }
    }

    fun patchProduct(
        id: Long,
        request: ProductPatchRequest,
    ): Product {
        return try {
            productRepository.patch(id, request)
        } catch (e: DataIntegrityViolationException) {
            if (e.message?.contains("name", ignoreCase = true) == true) {
                throw ProductValidationException("Product name already exists")
            }
            throw e
        }
    }

    fun findAll(): List<Product> = productRepository.findAll()

    fun findById(id: Long): Product = productRepository.findById(id)

    fun deleteById(id: Long) = productRepository.delete(id)
}
