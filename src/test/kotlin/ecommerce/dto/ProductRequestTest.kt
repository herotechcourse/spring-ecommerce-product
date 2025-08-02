package ecommerce.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductRequestTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid product should pass validation`() {
        val product =
            ProductRequest(
                name = "Ice Cream",
                price = 1.99,
                imageUrl = "https://example.com/image.jpg",
            )

        val violations = validator.validate(product)

        Assertions.assertThat(violations).isEmpty()
    }
}
