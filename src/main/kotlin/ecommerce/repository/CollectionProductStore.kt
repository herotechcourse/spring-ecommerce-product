package ecommerce.repository

import ecommerce.model.Product
import org.springframework.stereotype.Repository

@Repository
class CollectionProductStore(val products: MutableList<Product>) : ProductStore {
    override fun countProducts(): Int {
        return products.size
    }

    override fun findAll(): List<Product> {
        return products.toList()
    }

    override fun findById(id: Long): Product? {
        return products.firstOrNull { it.id == id } ?: throw NoSuchElementException("Product with id $id not found")
    }

    override fun save(product: Product) {
        val newProduct = Product(product.id, product.name, price = product.price, imageUrl = product.imageUrl)
        products.add(newProduct)
    }

    override fun update(
        id: Long,
        product: Product,
    ): Int {
        val updatedProduct = findById(id)
        updatedProduct?.name = product.name
        updatedProduct?.price = product.price
        updatedProduct?.imageUrl = product.imageUrl
        return 1
    }

    override fun delete(id: Long): Int {
        val deletedProduct = findById(id)
        products.remove(deletedProduct)
        return 1
    }
}