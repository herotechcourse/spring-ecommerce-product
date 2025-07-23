package ecommerce.product.data

import ecommerce.CustomAssertExtension.shouldContainViolation
import ecommerce.TestFixture.InvalidRequest.EXCEED_NAME_AMERICANO
import ecommerce.TestFixture.ValidRequest.AMERICANO
import ecommerce.view.ValidationMessages.Invalid.NAME_MUST_LENGTH
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductRequestTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `valid ProductRequest should pass validation`() {
        val request = AMERICANO
        val violations = validator.validate(request)

        assertThat(violations).isEmpty()
    }
}
