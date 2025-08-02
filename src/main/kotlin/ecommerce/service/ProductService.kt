package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.entity.Product
import ecommerce.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductService(private val productRepository: ProductRepository) {
    private val namePattern = Regex("^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$")
    private val minNameLength = 1
    private val maxNameLength = 15

    private fun validateUniqueName(name: String) {
        if (productRepository.existsByName(name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name must be unique.")
        }
    }

    private fun validateName(name: String) {
        if (name.length !in minNameLength..maxNameLength) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be between $minNameLength and $maxNameLength characters.")
        }
        if (!namePattern.matches(name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Name contains invalid special characters.")
        }
    }

    private fun validateImageUrl(imageUrl: String) {
        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Image URL must start with http:// or https://")
        }
    }

    fun createProduct(request: ProductRequest): Long {
        validateName(request.name)
        validateUniqueName(request.name)
        validateImageUrl(request.imageUrl)
        return productRepository.create(request.toEntity())
    }

    fun getAllProducts(): List<Product> {
        return productRepository.getAll()
    }

    fun updateProduct(
        id: Long,
        request: ProductRequest,
    ) {
        validateName(request.name)
        validateUniqueName(request.name)
        val updated = productRepository.update(id, request.toEntity(id))
        if (!updated) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.")
    }

    fun deleteProduct(id: Long) {
        val deleted = productRepository.delete(id)
        if (!deleted) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.")
    }
}
