package ecommerce.validation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProductNameValidatorTest {
    private val validator = ProductNameValidator()

    @Test
    fun `should return true for valid product names`() {
        val validNames = listOf("Apple Juice", "Vitamin-C", "Test+Item", "Nice Product")

        validNames.forEach {
            assertThat(validator.isValid(it, null)).isTrue()
        }
    }

    @Test
    fun `should return false for names with special characters`() {
        val invalidNames = listOf("Invalid@Name", "Bad#Item", "Wrong!Name", "Name$")

        invalidNames.forEach {
            assertThat(validator.isValid(it, null)).isFalse()
        }
    }

    @Test
    fun `should return false for names longer than 20 characters`() {
        val longName = "ThisNameIsWayTooLongToBeValid"
        assertThat(validator.isValid(longName, null)).isFalse()
    }

    @Test
    fun `should return false for null name`() {
        assertThat(validator.isValid(null, null)).isFalse()
    }

    @Test
    fun `should return false for blank name`() {
        assertThat(validator.isValid("   ", null)).isFalse()
    }
}
