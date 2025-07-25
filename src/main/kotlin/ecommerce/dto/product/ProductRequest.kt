package ecommerce.dto.product

import ecommerce.validation.NAME_LENGTH_MAXIMUM
import ecommerce.validation.PRODUCT_NAME_PATTERN
import ecommerce.validation.PRODUCT_PRICE_MINIMUM
import ecommerce.validation.URL_PATTERN
import ecommerce.validation.UniqueProductName
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ProductRequest(
    @field:Size(max = NAME_LENGTH_MAXIMUM, message = ErrorMessages.NAME_SIZE)
    @field:NotBlank(message = ErrorMessages.NAME_BLANK)
    @field:Pattern(
        regexp = PRODUCT_NAME_PATTERN,
        message = ErrorMessages.NAME_PATTERN,
    )
    @field:UniqueProductName
    val name: String,
    @field:DecimalMin(value = PRODUCT_PRICE_MINIMUM, message = ErrorMessages.PRICE_MIN)
    val price: Double,
    @field:NotBlank(message = ErrorMessages.IMAGE_URL_BLANK)
    @field:Pattern(
        regexp = URL_PATTERN,
        message = ErrorMessages.IMAGE_URL_PATTERN,
    )
    val imageUrl: String,
) {
    companion object {
        object ErrorMessages {
            const val NAME_SIZE = "Product name must be shorter than 15 characters"
            const val NAME_BLANK = "Product name cannot be blank"
            const val NAME_PATTERN = "Product name contains invalid characters"
            const val PRICE_MIN = "Product price must be greater than 0"
            const val IMAGE_URL_BLANK = "Product image URL cannot be blank"
            const val IMAGE_URL_PATTERN = "Product image URL must start with http:// or https://"
        }
    }
}
