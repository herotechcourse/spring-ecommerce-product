package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.model.Product
import ecommerce.repository.ProductStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ProductService(
    @Qualifier("jdbcProductStore") private val productRepository: ProductStore,
) {
    fun createProduct(productRequest: ProductRequest): Product {
        if (productRepository.existsByName(productRequest.name)) {
            throw IllegalArgumentException("Product with name '${productRequest.name}' already exists.")
        }
        val product =
            Product(
                name = productRequest.name,
                price = productRequest.price,
                imageUrl = productRequest.imageUrl,
            )
        return productRepository.save(product)
    }
}
