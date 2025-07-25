package ecommerce.validation

import ecommerce.repository.ProductRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component

@Component
class UniqueProductNameValidator(
    private val productRepository: ProductRepository,
) : ConstraintValidator<UniqueProductName, String> {
    override fun isValid(
        name: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {
        return name?.let { !productRepository.existsByName(it) } ?: true
    }
}
