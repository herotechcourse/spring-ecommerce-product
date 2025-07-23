package ecommerce.product.data

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductRequestTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `valid ProductRequest should pass validation`() {
        val request =
            ProductRequest(
                name = "Coffee",
                price = BigDecimal("10.50"),
                imageUrl = "https://example.com/image.jpg",
            )

        val violations = validator.validate(request)

        assertThat(violations).isEmpty()
    }
}
