package ecommerce.service

import ecommerce.model.Product
import ecommerce.store.ProductStore
import org.springframework.stereotype.Service

@Service
class ProductService(private val productStore: ProductStore) {

    fun findAll(): List<Product> = productStore.findAllProducts()

    fun findById(id: Long): Product? = productStore.findProductById(id)

    fun insert(product: Product) = productStore.insertProduct(product)

    fun update(id: Long, product: Product): Boolean {
        if (productStore.findProductById(id) == null) return false
        productStore.updateProduct(id, product)
        return true
    }

    fun delete(id: Long): Boolean {
        if (productStore.findProductById(id) == null) return false
        productStore.deleteProduct(id)
        return true
    }
}
