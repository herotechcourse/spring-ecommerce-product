package ecommerce.model

import jakarta.validation.constraints.*

data class ProductPatchDTO(

    @field:Size(min = 1, max = 15, message = "Name must have at maximum 15 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-&/_ ]+$", message = "Invalid characters")
    var name: String,

    @field:Positive(message = "Product price must be positive")
    var price: Double,

    @field:Pattern(
        regexp = "^https?://.*",
        message = "URL must start with http:// or https://"
    )
    var imageUrl: String,
)
