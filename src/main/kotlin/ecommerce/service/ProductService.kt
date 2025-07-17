package ecommerce.service

import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getAllProducts(): List<Product> = productRepository.findAllProducts()

    fun getProductById(id: Long): Product? = productRepository.findById(id)

    fun createProduct(newProduct: Product) {
        productRepository.create(newProduct)
    }

    fun updateProduct(
        id: Long,
        updatedProduct: Product,
    ) {
        productRepository.update(id, updatedProduct)
    }

    fun deleteProduct(id: Long) = productRepository.delete(id)
}
