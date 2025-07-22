package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse

interface ProductService {
    fun findById(id: Long): ProductResponse?

    fun findAll(): List<ProductResponse>

    fun createProduct(productRequest: ProductRequest): Boolean

    fun existsById(id: Long): Boolean

    fun updateProduct(
        id: Long,
        productRequest: ProductRequest,
    )

    fun deleteProduct(id: Long): Boolean
}
