package ecommerce.service

import ecommerce.model.Product
import ecommerce.store.ProductStore
import org.springframework.stereotype.Service

@Service
class ProductService(private val productStore: ProductStore) {

    fun findAll(): List<Product> = productStore.findAllProducts()

    fun findById(id: Long): Product? = productStore.findProductById(id)

    fun insert(product: Product) = productStore.insertProduct(product)

    fun update(
        id: Long,
        product: Product,
    ) = productStore.updateProduct(id, product)

    fun delete(id: Long): Int = productStore.deleteProduct(id)
}
