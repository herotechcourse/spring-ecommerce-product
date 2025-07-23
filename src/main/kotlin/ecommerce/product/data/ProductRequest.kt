package ecommerce.product.data

import ecommerce.product.data.ConstantsProduct.Validation.IMAGE_URL_MAX_LENGTH
import ecommerce.product.data.ConstantsProduct.Validation.IMAGE_URL_PATTERN
import ecommerce.product.data.ConstantsProduct.Validation.NAME_ALLOWED_PATTERN
import ecommerce.product.data.ConstantsProduct.Validation.NAME_MAX_LENGTH
import ecommerce.product.data.ConstantsProduct.Validation.PRICE_DECIMAL_SCALE
import ecommerce.product.data.ConstantsProduct.Validation.PRICE_MIN
import ecommerce.view.ValidationMessages.Invalid.IMAGE_URL_MUST_LENGTH
import ecommerce.view.ValidationMessages.Invalid.IMAGE_URL_MUST_PATTERN
import ecommerce.view.ValidationMessages.Invalid.NAME_MUST_LENGTH
import ecommerce.view.ValidationMessages.Invalid.NAME_MUST_PATTERN
import ecommerce.view.ValidationMessages.Invalid.PRICE_MUST_GREATER
import ecommerce.view.ValidationMessages.Invalid.PRICE_MUST_SCALE
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal

class ProductRequest(
    @field:Size(max = NAME_MAX_LENGTH, message = NAME_MUST_LENGTH)
    @field:Pattern(
        regexp = NAME_ALLOWED_PATTERN,
        message = NAME_MUST_PATTERN,
    )
    val name: String,
    @field:DecimalMin(PRICE_MIN, message = PRICE_MUST_GREATER)
    @field:Digits(integer = 10, fraction = PRICE_DECIMAL_SCALE, message = PRICE_MUST_SCALE)
    val price: BigDecimal,
    @field:Size(max = IMAGE_URL_MAX_LENGTH, message = IMAGE_URL_MUST_LENGTH)
    @field:Pattern(
        regexp = IMAGE_URL_PATTERN,
        message = IMAGE_URL_MUST_PATTERN,
    )
    val imageUrl: String,
)
