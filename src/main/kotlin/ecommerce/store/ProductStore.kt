package ecommerce.store

import ecommerce.dto.ProductPatchDTO
import ecommerce.model.Product

interface ProductStore {
    fun findAllProducts(): List<Product>

    fun findProductById(id: Long): Product?

    fun insertProduct(product: Product): Product

    fun patchProduct(
        id: Long,
        patch: ProductPatchDTO,
    )

    fun deleteProduct(id: Long): Int
}
