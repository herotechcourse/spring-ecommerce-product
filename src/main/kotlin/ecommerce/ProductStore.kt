package ecommerce

import ecommerce.model.Product

interface ProductStore {
    fun countProducts(): Int
    fun findAll(): List<Product>
    fun findById(id: Long): Product?
    fun save(product: Product)
    fun update(id: Long, product: Product)
    fun delete(id: Long): Int
}