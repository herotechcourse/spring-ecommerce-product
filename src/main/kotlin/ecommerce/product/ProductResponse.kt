package ecommerce.product

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: String,
    val imageUrl: String,
)
