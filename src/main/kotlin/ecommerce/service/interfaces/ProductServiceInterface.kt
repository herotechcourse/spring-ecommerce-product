package ecommerce.service.interfaces

import ecommerce.dto.ProductDTO
import org.springframework.http.ResponseEntity

interface ProductServiceInterface {
    fun getAllProducts(): ResponseEntity<List<ProductDTO>>

    fun getProductById(id: Long): ResponseEntity<ProductDTO>

    fun createProduct(product: ProductDTO): ResponseEntity<Void>

    fun updateProduct(
        id: Long,
        product: ProductDTO,
    ): ResponseEntity<Void>

    fun deleteProduct(id: Long): ResponseEntity<Void>
}
