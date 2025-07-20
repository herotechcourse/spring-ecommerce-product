package ecommerce

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductTest {

    @Test
    fun `should create product when name and imageUrl are within limits`() {
        val product = Product(
            name = "Vanilla Ice Cream",
            price = 1.99,
            imageUrl = "https://example.com/vanilla.jpg"
        )

        assertEquals("Vanilla Ice Cream", product.name)
        assertEquals(1.99, product.price)
        assertEquals("https://example.com/vanilla.jpg", product.imageUrl)
        assertNull(product.id)
    }

    @Test
    fun `should throw when name exceeds 255 characters`() {
        val longName = "a".repeat(256)

        assertThrows(RuntimeException::class.java) {
            Product(
                name = longName,
                price = 2.49,
                imageUrl = "https://example.com/pistachio.jpg"
            )
        }
    }

    @Test
    fun `should throw when imageUrl exceeds 512 characters`() {
        val longUrl = "https://example.com/" + "a".repeat(512)

        assertThrows(RuntimeException::class.java) {
            Product(
                name = "Chocolate Ice Cream",
                price = 1.49,
                imageUrl = longUrl
            )
        }
    }
}

