package ecommerce.domain

import java.math.BigDecimal

class NewProduct(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String,
) {
    init {
        // TODO: logic
    }
}
