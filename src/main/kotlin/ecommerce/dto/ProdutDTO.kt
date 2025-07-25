package ecommerce.dto

import java.math.BigDecimal

data class ProductDTO(
    val name: String?,
    val price: BigDecimal?,
    val imageUrl: String?
) {
    fun validate(): ProductDTO {
        require(!name.isNullOrBlank()) { "Name must not be blank." }
        require(price != null && price > BigDecimal.ZERO) { "Price must be greater than 0." }
        require(!imageUrl.isNullOrBlank() && imageUrl.startsWith("http")) { "Invalid image URL." }
        return this
    }
}
