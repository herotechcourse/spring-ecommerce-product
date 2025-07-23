package ecommerce.model

data class ProductPatchRequest(
    val name: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null,
)
