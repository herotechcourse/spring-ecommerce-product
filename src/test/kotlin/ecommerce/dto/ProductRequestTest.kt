package ecommerce.dto

import ecommerce.helper.ProductTestFixture.InvalidRequest.INVALID_IMAGE_URL_CHARACTERS
import ecommerce.helper.ProductTestFixture.InvalidRequest.INVALID_IMAGE_URL_EXCEED
import ecommerce.helper.ProductTestFixture.InvalidRequest.INVALID_NAME_CHARACTERS
import ecommerce.helper.ProductTestFixture.InvalidRequest.INVALID_NAME_EXCEED
import ecommerce.helper.ProductTestFixture.InvalidRequest.INVALID_PRICE_TOO_SMALL
import ecommerce.helper.ProductTestFixture.ValidRequest.AMERICANO
import ecommerce.view.ValidationMessages.Invalid.IMAGE_URL_MUST_LENGTH
import ecommerce.view.ValidationMessages.Invalid.IMAGE_URL_MUST_PATTERN
import ecommerce.view.ValidationMessages.Invalid.NAME_MUST_LENGTH
import ecommerce.view.ValidationMessages.Invalid.NAME_MUST_PATTERN
import ecommerce.view.ValidationMessages.Invalid.PRICE_MUST_GREATER
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
    fun `ProductRequest should pass validation`() {
        val request = AMERICANO
        val violations = validator.validate(request)

        assertThat(violations).isEmpty()
    }

    @Test
    fun `name exceeding max length should fail size validation`() {
        val request = INVALID_NAME_EXCEED
        val violations = validator.validate(request)

        assertThat(violations).anyMatch {
            it.propertyPath.toString() == "name" && it.message == NAME_MUST_LENGTH
        }
    }

    @Test
    fun `name with invalid characters should fail pattern validation`() {
        val request = INVALID_NAME_CHARACTERS
        val violations = validator.validate(request)

        assertThat(violations).anyMatch {
            it.propertyPath.toString() == "name" && it.message == NAME_MUST_PATTERN
        }
    }

    @Test
    fun `price below minimum should fail decimal min validation`() {
        val request = INVALID_PRICE_TOO_SMALL
        val violations = validator.validate(request)

        assertThat(violations).anyMatch {
            it.propertyPath.toString() == "price" && it.message == PRICE_MUST_GREATER
        }
    }

    @Test
    fun `imageUrl not starting with http should fail pattern validation`() {
        val request = INVALID_IMAGE_URL_CHARACTERS
        val violations = validator.validate(request)

        assertThat(violations).anyMatch {
            it.propertyPath.toString() == "imageUrl" && it.message == IMAGE_URL_MUST_PATTERN
        }
    }

    @Test
    fun `imageUrl exceeding max length should fail size validation`() {
        val request = INVALID_IMAGE_URL_EXCEED
        val violations = validator.validate(request)

        assertThat(violations).anyMatch {
            it.propertyPath.toString() == "imageUrl" && it.message == IMAGE_URL_MUST_LENGTH
        }
    }
}
