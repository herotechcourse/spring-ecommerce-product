package ecommerce.service

import ecommerce.domain.Product
import ecommerce.exception.InvalidInputException
import ecommerce.exception.ResourceNotFoundException
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getAllProducts(): List<Product> = productRepository.findAllProducts()

    fun getProductById(id: Long): Product {
        require(id <= 0) { "Product ID must be greater than or equal to 1" }
        return productRepository.findById(id) ?: throw ResourceNotFoundException("Product", "id", id)
    }

    fun createProduct(newProduct: Product) {
        if (productRepository.findByName(newProduct.name) != null) {
            throw InvalidInputException("Product name ${newProduct.name} already exists")
        }

        productRepository.create(newProduct)
    }

    fun updateProduct(
        id: Long,
        updatedProduct: Product,
    ) {
        require(id <= 0) { "Product ID must be greater than or equal to 1" }

        productRepository.findById(id)
            ?: throw ResourceNotFoundException("Product", "id", id)

        productRepository.update(id, updatedProduct)
    }

    fun deleteProduct(id: Long) = productRepository.delete(id)
}
