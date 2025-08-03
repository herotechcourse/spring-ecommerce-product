package ecommerce.repository

import ecommerce.model.Product

interface ProductRepository {
    fun getAll(): List<Product>

    fun create(product: Product): Long?

    fun update(
        id: Long,
        product: Product,
    ): Boolean

    fun delete(id: Long): Boolean

    fun existsByName(product: Product): Boolean
}
