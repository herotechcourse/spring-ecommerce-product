package ecommerce.dto

import java.math.BigDecimal

class DummyProductRequest(
    val name: String? = null,
    val price: BigDecimal? = null,
    val imageUrl: String? = null,
)
