package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.entity.Product
import ecommerce.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun validateUniqueName(name: String) {
        if (productRepository.existsByName(name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name must be unique.")
        }
    }

    fun createProduct(request: ProductRequest): Long {
        validateUniqueName(request.name)
        return productRepository.create(request.toEntity())
    }

    fun getAllProducts(): List<Product> {
        return productRepository.getAll()
    }

    fun updateProduct(
        id: Long,
        request: ProductRequest,
    ) {
        validateUniqueName(request.name)
        val updated = productRepository.update(id, request.toEntity(id))
        if (!updated) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.")
    }

    fun deleteProduct(id: Long) {
        val deleted = productRepository.delete(id)
        if (!deleted) throw RuntimeException("Product not found.")
    }
}
