package ecommerce.service

import ecommerce.dto.ProductDTO
import ecommerce.model.Product
import ecommerce.repository.BaseProductStore
import ecommerce.repository.ProductStore
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(private val repository: ProductStore, private val productStore: BaseProductStore) {

    fun validateName(name: String)  {
        val regex = Regex("^[()\\[\\]+\\-&/_a-zA-Z0-9\\s]+\$")
        val existingProduct = productStore.findByName(name)

        if (existingProduct != null) {
            throw IllegalArgumentException("Product name already exists.")
        }

        require(!name.isBlank()) {"Product name cannot be empty."}
        require(name.length <= 15) {"Product name cannot be more than 15 characters."}
        require(regex.matches(name)) {"Name input can contain only ( ), [ ], +, -, &, /, _, special characters, numbers and letters. "}
    }

    fun validatePrice(price: BigDecimal) {
        require(price.compareTo(BigDecimal.ZERO) > 0) {"Product price must be greater than 0."}
    }

    fun validateUrl(imageUrl: String) {
        require(imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {"Image URL must start with http:// or https://"}
    }

    fun createProduct(product: Product): Product {
        val entity = Product.toEntity(product)
        validateName(entity.name)
        validatePrice(entity.price)
        validateUrl(entity.imageUrl)
        return repository.insert(entity)
    }

    fun getAllProducts(): List<Product> {
        return if (repository.isEmptyOrNull()) emptyList() else repository.findAll()
    }

    fun updateProduct(id: Long, dto: ProductDTO): Product? {
        dto.validate().apply {
            name
            price
            imageUrl
        }

        val existing = repository[id] ?: return null
        val updated = existing.updateWith(dto)
        repository.updateById(id, updated)
        return updated
    }

    fun deleteProductById(id: Long): Boolean {
        return repository.deleteById(id) != null
    }

}
