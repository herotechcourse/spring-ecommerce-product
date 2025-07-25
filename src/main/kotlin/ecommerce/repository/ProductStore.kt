package ecommerce.repository

import ecommerce.model.Product

interface ProductStore {
    fun countProducts(): Int

    fun findAll(): List<Product>

    fun findById(id: Long): Product?

    fun findByName(name: String): Product?

    fun save(product: Product)

    fun update(
        id: Long,
        product: Product,
    ): Int

    fun delete(id: Long): Int
}
