package ecommerce.dao

import ecommerce.model.Product

interface ProductDAO {
    fun findAll(): List<Product>

    fun findById(id: Long): Product?

    fun insert(product: Product)

    fun update(product: Product): Int

    fun delete(id: Long): Int
}
