package ecommerce.product.data

import java.math.BigDecimal

data class ProductRequest(

    val name: String,

    val price: BigDecimal,

    val imageUrl: String,
)

fun ProductRequest.toEntity(id: Long): Product {
    return Product(
        id = id,
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
    )
}
