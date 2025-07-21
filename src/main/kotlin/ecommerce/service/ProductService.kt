package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse

interface ProductService {
    fun findById(id: Long): ProductResponse?

    fun findAll(): List<ProductResponse>

    fun createProduct(productRequest: ProductRequest): ProductResponse

    fun updateProduct(
        id: Long,
        productRequest: ProductRequest,
    ): ProductResponse?

    fun deleteProduct(id: Long): Boolean
}
