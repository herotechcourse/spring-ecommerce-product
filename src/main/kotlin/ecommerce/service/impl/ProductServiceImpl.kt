package ecommerce.service.impl

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.exception.ProductNotFoundException
import ecommerce.model.toDto
import ecommerce.repository.ProductRepository
import ecommerce.service.ProductService
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {
    override fun findById(id: Long): ProductResponse? {
        val product = productRepository.findById(id) ?: throw ProductNotFoundException("Product not found, id: $id")
        return product.toDto()
    }

    override fun findAll(): List<ProductResponse> {
        TODO("Not yet implemented")
    }

    override fun createProduct(productRequest: ProductRequest): ProductResponse {
        TODO("Not yet implemented")
    }

    override fun updateProduct(
        id: Long,
        productRequest: ProductRequest,
    ): ProductResponse? {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(id: Long): Boolean {
        TODO("Not yet implemented")
    }
}
