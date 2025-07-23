package ecommerce.products.store

import ecommerce.products.model.Product
import ecommerce.products.model.ProductPatchDTO

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
