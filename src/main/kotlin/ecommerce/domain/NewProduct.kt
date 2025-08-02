package ecommerce.domain

import ecommerce.constants.ConstantsProduct.Validation.IMAGE_URL_MAX_LENGTH
import ecommerce.constants.ConstantsProduct.Validation.NAME_MAX_LENGTH
import ecommerce.constants.ConstantsProduct.Validation.PRICE_DECIMAL_SCALE
import java.math.BigDecimal

class NewProduct(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String,
) {
    init {
        require(name.isNotBlank() && name.length in 2..NAME_MAX_LENGTH)
        require(name.matches(Regex("^[a-zA-Z0-9\\s()\\[\\]+\\-&/_]+$")))
        require(price > BigDecimal.ZERO && price.scale() == PRICE_DECIMAL_SCALE)
        require(imageUrl.isNotBlank() && imageUrl.length in 1..IMAGE_URL_MAX_LENGTH)
    }
}
