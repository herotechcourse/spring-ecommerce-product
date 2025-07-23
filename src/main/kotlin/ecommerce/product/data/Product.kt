package ecommerce.product.data

import java.math.BigDecimal

class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String,
)
