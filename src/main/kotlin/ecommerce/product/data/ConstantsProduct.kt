package ecommerce.product.data

object ConstantsProduct {

    object Validation {
        // letters, digits, space, (), [], +, -, &, /, _
        const val PRODUCT_NAME_PATTERN = "^[\\w\\s\\[\\]\\(\\)\\+\\-&/_]*$"

        const val SCHEMA_SQL_URL_LIMIT = 255
    }
}
