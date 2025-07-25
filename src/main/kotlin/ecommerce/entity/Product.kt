package ecommerce.entity

import java.math.BigDecimal

class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String,
)
