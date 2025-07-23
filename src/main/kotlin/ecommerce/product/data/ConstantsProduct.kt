package ecommerce.product.data

object ConstantsProduct {
    object Validation {
        // letters, digits, space, (), [], +, -, &, /, _
        const val NAME_ALLOWED_PATTERN = "^[\\w\\s\\[\\]\\(\\)\\+\\-&/_]*$"
        const val NAME_MAX_LENGTH = 15
        const val IMAGE_URL_MAX_LENGTH = 2000
        const val PRICE_MIN = "0.10"
        const val PRICE_DECIMAL_SCALE = 2
        const val IMAGE_URL_PATTERN = "^(http|https)://.*$"
    }
}
