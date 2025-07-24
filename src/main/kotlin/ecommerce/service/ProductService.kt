package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.exception.ProductAlreadyInDBException
import ecommerce.exception.ProductCreationException
import ecommerce.exception.ProductNotFoundException
import ecommerce.exception.ProductUpdateException
import ecommerce.model.toDto
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun findById(id: Long): ProductResponse? {
        val product = productRepository.findById(id) ?: throw ProductNotFoundException("Product not found, id: $id")
        return product.toDto()
    }

    fun findAll(): List<ProductResponse> {
        val products = productRepository.getAll()
        return products.map { it.toDto() }
    }

    fun createProduct(productRequest: ProductRequest): Boolean {
        if (productRepository.existsByName(
                productRequest.name,
            )
        ) {
            throw ProductAlreadyInDBException("Product already exists with name: ${productRequest.name}")
        }
        val created = productRepository.createProduct(productRequest)
        return if (created) {
            true
        } else {
            throw ProductCreationException("Failed to create product")
        }
    }

    fun existsById(id: Long): Boolean {
        return productRepository.existsById(id)
    }

    fun updateProduct(
        id: Long,
        productRequest: ProductRequest,
    ) {
        if (!productRepository.existsById(id)) throw ProductNotFoundException("Product not found, id: $id")
        val updated = productRepository.updateProduct(id, productRequest)
        if (!updated) throw ProductUpdateException("Failed to update product, id: $id")
    }

    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) throw ProductNotFoundException("Product not found, id: $id")
        val deleted = productRepository.deleteProduct(id)
        if (!deleted) throw ProductUpdateException("Failed to delete product, id: $id")
    }
}
