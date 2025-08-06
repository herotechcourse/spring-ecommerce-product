package ecommerce.service

import ecommerce.dto.product.ProductPatchRequest
import ecommerce.dto.product.ProductRequest
import ecommerce.exception.ProductValidationException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    private fun validateProductNameUniqueness(
        name: String,
        excludeId: Long? = null,
    ) {
        if (excludeId != null) {
            val existingProduct = productRepository.findById(excludeId)
            if (existingProduct.name != name && productRepository.existsByName(name)) {
                throw ProductValidationException("Product name already exists")
            }
        } else {
            if (productRepository.existsByName(name)) {
                throw ProductValidationException("Product name already exists")
            }
        }
    }

    fun createProduct(request: ProductRequest): Product {
        validateProductNameUniqueness(request.name)

        val product =
            Product(
                name = request.name,
                price = request.price,
                imageUrl = request.imageUrl,
            )
        return productRepository.save(product)
    }

    fun updateProduct(
        id: Long,
        request: ProductRequest,
    ): Product {
        validateProductNameUniqueness(request.name, id)

        val product =
            Product(
                id = id,
                name = request.name,
                price = request.price,
                imageUrl = request.imageUrl,
            )
        return productRepository.update(id, product)
    }

    fun patchProduct(
        id: Long,
        request: ProductPatchRequest,
    ): Product {
        request.name?.let { newName ->
            validateProductNameUniqueness(newName, id)
        }

        return productRepository.patch(id, request)
    }

    fun findAll(): List<Product> = productRepository.findAll()

    fun findById(id: Long): Product = productRepository.findById(id)

    fun deleteById(id: Long) = productRepository.delete(id)
}
