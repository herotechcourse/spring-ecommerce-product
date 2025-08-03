package ecommerce.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductTest {
    @Test
    fun `name greater 15 characters should fail validation`() {
        assertThrows<IllegalArgumentException> {
            Product(
                "a".repeat(16),
                1.2,
                "http://example.jpg",
            )
        }
    }

    @Test
    fun `name contains not allowed characters should fail validation`() {
        assertThrows<IllegalArgumentException> {
            Product(
                "#a",
                1.2,
                "http://example.jpg",
            )
        }
    }

    @Test
    fun `empty name should fail validation`() {
        assertThrows<IllegalArgumentException> {
            Product(
                "",
                1.2,
                "http://example.jpg",
            )
        }
    }

    @Test
    fun `price zero should fail validation`() {
        assertThrows<IllegalArgumentException> {
            Product(
                "a",
                0.0,
                "http://example.jpg",
            )
        }
    }

    @Test
    fun `link to image without http prefix should fail validation`() {
        assertThrows<IllegalArgumentException> {
            Product(
                "a",
                3.0,
                "example.jpg",
            )
        }
    }
}
