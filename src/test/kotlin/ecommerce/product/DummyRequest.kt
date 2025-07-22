package ecommerce.product

import java.math.BigDecimal

data class DummyRequest(
    val name: String? = null,
    val price: BigDecimal? = null,
    val imageUrl: String? = null,
)
