package ecommerce.service

import ecommerce.model.Product
import ecommerce.model.ProductPatchDTO
import ecommerce.store.ProductStore
import org.springframework.stereotype.Service

@Service
class ProductService(private val productStore: ProductStore) {
    fun findAll(): List<Product> = productStore.findAllProducts()

    fun findById(id: Long): Product? = productStore.findProductById(id)

    fun insert(product: Product): Product = productStore.insertProduct(product)

    fun update(
        id: Long,
        dto: ProductPatchDTO,
    ): Boolean {
        val product = productStore.findProductById(id) ?: return false
        product.updateFrom(dto)
        productStore.patchProduct(id, dto)
        return true
    }

    fun delete(id: Long): Boolean {
        if (productStore.findProductById(id) == null) return false
        productStore.deleteProduct(id)
        return true
    }
}
