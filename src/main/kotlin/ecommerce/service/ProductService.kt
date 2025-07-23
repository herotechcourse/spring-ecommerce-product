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
        if (id <= 0) throw IllegalArgumentException("Product ID must be greater than or equal to 1")
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
        if (id <= 0) throw IllegalArgumentException("Product ID must be greater than or equal to 1")

        val existingProduct =
            productRepository.findById(id)
                ?: throw ResourceNotFoundException("Product", "id", id)

        if (updatedProduct.name != existingProduct.name) {
            if (productRepository.findByName(updatedProduct.name) != null) {
                throw InvalidInputException("Product with name '${updatedProduct.name}' already exists. Name must be unique.")
            }
        }

        productRepository.update(id, updatedProduct)
    }

    fun decreaseProductStock(
        id: Long,
        amount: Int,
    ) {
        val existingProduct =
            productRepository.findById(id)
                ?: throw ResourceNotFoundException("Product", "id", id)

        if (amount <= 0) {
            throw IllegalArgumentException("Decrease amount must be positive.")
        }

        if (existingProduct.quantity < amount) {
            throw IllegalStateException("Decrease amount must be positive.")
        }

        existingProduct.quantity -= amount
        productRepository.update(id, existingProduct)
    }

    fun deleteProduct(id: Long) = productRepository.delete(id)
}
