package ecommerce.dto

import jakarta.validation.constraints.NotBlank

class ProductRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val price: Double,
    @field:NotBlank
    val imageUrl: String,
    var id: Long? = null,
)
