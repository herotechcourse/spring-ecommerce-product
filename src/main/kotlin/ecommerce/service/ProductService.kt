package ecommerce.service

import ecommerce.exception.ProductValidationException
import ecommerce.model.Product
import ecommerce.dto.ProductPatchDTO
import ecommerce.store.ProductStore
import org.springframework.stereotype.Service

@Service
class ProductService(private val productStore: ProductStore) {
    fun findAll(): List<Product> = productStore.findAllProducts()

    fun findById(id: Long): Product? = productStore.findProductById(id)

    fun insert(product: Product): Product {
        val allProducts = findAll()
        val nameAlreadyExists = allProducts.any { it.name == product.name }
        if (nameAlreadyExists) {
            throw ProductValidationException("Product with name '${product.name}' already exists.")
        }
        return productStore.insertProduct(product)
    }

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
