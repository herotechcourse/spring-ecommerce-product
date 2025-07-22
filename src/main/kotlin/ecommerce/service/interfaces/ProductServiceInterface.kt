package ecommerce.service.interfaces

import ecommerce.dto.ProductDTO
import ecommerce.dto.ProductPatchDTO
import java.net.URI

interface ProductServiceInterface {
    fun getAllProducts(): List<ProductDTO>

    fun getProductById(id: Long): ProductDTO

    fun createProduct(product: ProductDTO): URI

    fun updateProduct(
        id: Long,
        product: ProductDTO,
    )

    fun patchProduct(
        id: Long,
        patch: ProductPatchDTO,
    )

    fun deleteProduct(id: Long)
}
