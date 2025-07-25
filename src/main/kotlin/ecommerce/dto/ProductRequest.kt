package ecommerce.dto

import ecommerce.constants.ConstantsProduct
import ecommerce.view.ValidationMessages
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal

class ProductRequest(
    @NotBlank(message = "Name ${ValidationMessages.Invalid.CAN_NOT_BE_EMPTY}")
    @field:Size(max = ConstantsProduct.Validation.NAME_MAX_LENGTH, message = ValidationMessages.Invalid.NAME_MUST_LENGTH)
    @field:Pattern(
        regexp = ConstantsProduct.Validation.NAME_ALLOWED_PATTERN,
        message = ValidationMessages.Invalid.NAME_MUST_PATTERN,
    )
    val name: String,
    @NotBlank(message = "Price ${ValidationMessages.Invalid.CAN_NOT_BE_EMPTY}")
    @field:DecimalMin(ConstantsProduct.Validation.PRICE_MIN, message = ValidationMessages.Invalid.PRICE_MUST_GREATER)
    @field:Digits(
        integer = 10,
        fraction = ConstantsProduct.Validation.PRICE_DECIMAL_SCALE,
        message = ValidationMessages.Invalid.PRICE_MUST_SCALE,
    )
    val price: BigDecimal,
    @NotBlank(message = "Image url ${ValidationMessages.Invalid.CAN_NOT_BE_EMPTY}")
    @field:Size(max = ConstantsProduct.Validation.IMAGE_URL_MAX_LENGTH, message = ValidationMessages.Invalid.IMAGE_URL_MUST_LENGTH)
    @field:Pattern(
        regexp = ConstantsProduct.Validation.IMAGE_URL_PATTERN,
        message = ValidationMessages.Invalid.IMAGE_URL_MUST_PATTERN,
    )
    val imageUrl: String,
)
