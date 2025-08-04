package ecommerce.service

import ecommerce.dto.ProductPatchDTO
import ecommerce.dto.ProductRequestDTO
import ecommerce.exception.ProductValidationException
import ecommerce.model.Product
import ecommerce.store.ProductStore
import org.springframework.stereotype.Service

@Service
class ProductService(private val productStore: ProductStore) {
    fun findAll(): List<Product> = productStore.findAllProducts()

    fun findById(id: Long): Product? = productStore.findProductById(id)

    fun insert(productRequest: ProductRequestDTO): Product {
        val allProducts = findAll()
        val nameAlreadyExists = allProducts.any { it.name == productRequest.name }
        if (nameAlreadyExists) {
            throw ProductValidationException("Product with name '${productRequest.name}' already exists.")
        }
        return productStore.insertProduct(productRequest.toEntity())
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
