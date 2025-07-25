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

    @Test
    fun `name longer than 15 characters should fail`() {
        val product =
            ProductRequest(
                name = "This name is way too long",
                price = 1.99,
                imageUrl = "https://example.com/image.jpg",
            )

        val violations = validator.validate(product)

        Assertions.assertThat(violations).hasSize(1)
        Assertions.assertThat(violations.first().message).isEqualTo("Name must not exceed 15 characters.")
    }

    @Test
    fun `name with invalid characters should fail`() {
        val product =
            ProductRequest(
                name = "Invalid@Name!",
                price = 1.99,
                imageUrl = "https://example.com/image.jpg",
            )

        val violations = validator.validate(product)

        Assertions.assertThat(violations).hasSize(1)
        Assertions.assertThat(violations.first().message).isEqualTo("Name contains invalid special characters.")
    }

    @Test
    fun `price less than or equal to 0 should fail`() {
        val product =
            ProductRequest(
                name = "Valid Name",
                price = 0.0,
                imageUrl = "https://example.com/image.jpg",
            )

        val violations = validator.validate(product)

        Assertions.assertThat(violations).hasSize(1)
        Assertions.assertThat(violations.first().message).isEqualTo("Price must be greater than 0.")
    }

    @Test
    fun `imageUrl that does not start with http or https should fail`() {
        val product =
            ProductRequest(
                name = "Valid Name",
                price = 1.0,
                imageUrl = "ftp://example.com/image.jpg",
            )

        val violations = validator.validate(product)

        Assertions.assertThat(violations).hasSize(1)
        Assertions.assertThat(violations.first().message).isEqualTo("Image URL must start with http:// or https://")
    }

    @Test
    fun `all validations fail together`() {
        val product =
            ProductRequest(
                name = "ThisNameIsWayTooLong!!!",
                price = 0.0,
                imageUrl = "invalid-url",
            )

        val violations = validator.validate(product)

        Assertions.assertThat(violations).hasSize(4)

        val messages = violations.map { it.message }

        Assertions.assertThat(messages).containsExactlyInAnyOrder(
            "Name must not exceed 15 characters.",
            "Name contains invalid special characters.",
            "Price must be greater than 0.",
            "Image URL must start with http:// or https://",
        )
    }
}
