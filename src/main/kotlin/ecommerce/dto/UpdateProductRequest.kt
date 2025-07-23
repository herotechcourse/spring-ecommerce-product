package ecommerce.dto

data class UpdateProductRequest(
    val name: String,
    val price: Double,
    val img: String,
    val quantity: Int,
)
