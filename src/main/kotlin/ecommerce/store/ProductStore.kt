package ecommerce.store

import ecommerce.model.Product

interface ProductStore {
    fun findAllProducts(): List<Product>
    fun findProductById(id: Long): Product?
    fun insertProduct(product: Product)
    fun updateProduct(id: Long, product: Product)
    fun deleteProduct(id: Long): Int
}
