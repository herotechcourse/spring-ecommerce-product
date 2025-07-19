package ecommerce.product

import ecommerce.store.BaseProductStore
import org.springframework.stereotype.Service

@Service
class ProductService(private val productStore: BaseProductStore) {

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

    fun validatePrice(price: Double) {
        require( price > 0.0) {"Product price must be greater than 0."}
    }

    fun validateUrl(imageUrl: String) {
        require(imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {"Image URL must start with http:// or https://"}
    }

}
