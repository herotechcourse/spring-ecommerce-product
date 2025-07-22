package ecommerce.product

import java.math.BigDecimal

data class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String,
)

fun Product.toResponse(): ProductResponse {
    return ProductResponse(
        this.id,
        this.name,
        this.price.toPlainString(),
        this.imageUrl,
    )
}
