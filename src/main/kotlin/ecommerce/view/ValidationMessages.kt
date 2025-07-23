package ecommerce.view

import ecommerce.product.data.ConstantsProduct.Validation.PRICE_DECIMAL_SCALE

object ValidationMessages {
    object Invalid {
        const val NAME_MUST_PATTERN = "Invalid characters in name"
        const val PRICE_MUST_GREATER = "Price must be greater than 0"
        const val PRICE_MUST_SCALE = "Price must have up to $PRICE_DECIMAL_SCALE} decimal places"
        const val IMAGE_URL_MUST_PATTERN = "Image URL must start with `http://` or `https://`"
    }
}
