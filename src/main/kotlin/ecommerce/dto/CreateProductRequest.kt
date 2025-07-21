package ecommerce.dto

data class CreateProductRequest(
    val name: String,
    val price: Double,
    val img: String,
    val quantity: Int,
)
