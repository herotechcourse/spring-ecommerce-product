package ecommerce.service

import ecommerce.exception.ConflictException
import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun create(product: Product): Long {
        if (productRepository.existsByName(product.name)) {
            throw ConflictException("Product with name ${product.name} already exists")
        }
        val id = productRepository.insertWithKeyHolder(product)
        return id
    }

    fun read(): List<Product> {
        val products = productRepository.findAllProducts()
        return products
    }

    fun upsert(
        newProduct: Product,
        id: Long,
    ): Boolean {
        if (!productRepository.existsById(id)) {
            create(newProduct)
            return true
        }
        if (!productRepository.update(newProduct, id)) {
            throw NotFoundException()
        }
        return false
    }

    fun delete(id: Long) {
        if (!productRepository.delete(id)) {
            throw NotFoundException()
        }
    }
}
