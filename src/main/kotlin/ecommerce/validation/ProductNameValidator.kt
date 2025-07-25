package ecommerce.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ProductNameValidator : ConstraintValidator<ValidProductName, String> {
    // Regex to allow letters, numbers, spaces, and these special chars: () [] + - & / _
    private val regex = Regex("^[a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/_]*$")

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {
        if (value.isNullOrBlank()) return false // reject null or blank names
        if (value.length > 15) return false // reject if longer than 15 chars
        return regex.matches(value)
    }
}
