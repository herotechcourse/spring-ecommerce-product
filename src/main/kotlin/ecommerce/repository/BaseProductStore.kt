package ecommerce.repository

import ecommerce.model.Product

interface BaseProductStore {
    fun isEmptyOrNull(): Boolean

    fun count(): Int?

    fun findAll(): List<Product>

    fun insert(product: Product): Product

    fun deleteById(id: Long): Int?

    fun updateById(
        id: Long,
        product: Product,
    ): Int?

    fun findByName(name: String): Product?

    operator fun get(id: Long): Product? // products[1] // fun findById(id: Long): Product? // products.findById(1)
}
