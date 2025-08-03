package ecommerce.dto

data class UpdateProductRequest(
    val productId: Long,
    val product: ProductDTO,
)
