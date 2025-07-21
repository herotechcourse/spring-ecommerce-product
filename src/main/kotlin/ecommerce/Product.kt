package ecommerce

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class Product(

    var id: Long? = null,
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(max = 15, message = "Product name must be 15 characters or less")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 \\(\\)\\[\\]\\+\\-&/_]+$",
        message = "Product name can only contain letters, numbers, spaces, and allowed special characters: (), [], +, -, &, /, _"
    )
    var name: String = "",
    var price: Double = 0.00,
    var imageUrl: String = "",
)
