package ecommerce.dto.product

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateProductRequest(
    @field:NotNull(message = "Name must not be blank")
    @field:Size(max = 15, message = "Name must be at most 15 characters")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,100}$",
        message =
            "Name must be 1–15 characters and only include letters, digits, spaces, " +
                "and allowed special characters:( ), [ ], +, -, &, /, _",
    )
    val name: String,
    @field:NotNull(message = "Price must not be null")
    @field:Min(1, message = "Price must be greater than 0")
    val price: Double,
    @field:NotNull(message = "Image Link must not be null")
    @field:Pattern(
        regexp = "^(http://|https://)[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=%]+$",
        message = "url must begin with http:// or https:// and be a valid URL",
    )
    val img: String,
    val quantity: Int,
)
