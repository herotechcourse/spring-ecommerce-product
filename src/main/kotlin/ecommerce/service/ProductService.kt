package ecommerce.service

import ecommerce.exception.product.DuplicateProductNameException
import ecommerce.exception.product.ProductNotFoundException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getAllProducts(): List<Product> = productRepository.findAllProducts()

    fun getProductById(id: Long): Product =
        productRepository.findById(id) ?: throw ProductNotFoundException("Product with ID $id not found")

    fun createProduct(newProduct: Product): Product? {
        if (productRepository.existsByName(newProduct.name)) {
            throw DuplicateProductNameException("Product name ${newProduct.name} already exists.")
        }
        productRepository.create(newProduct)
        return productRepository.findByName(newProduct.name)
    }

    fun updateProduct(
        id: Long,
        updatedProduct: Product,
    ) {
        productRepository.update(id, updatedProduct)
    }

    fun deleteProduct(id: Long) {
        productRepository.findById(id) ?: throw ProductNotFoundException("Product with ID $id not found")
        productRepository.delete(id)
    }
}
