package ecommerce

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ProductTest {
    @Test
    fun `should create a product`() {
        val product =
            Product(
                name = "Vanilla Ice Cream",
                price = 1.99,
                imageUrl = "https://example.com/vanilla.jpg",
            )

        assertEquals("Vanilla Ice Cream", product.name)
        assertEquals(1.99, product.price)
        assertEquals("https://example.com/vanilla.jpg", product.imageUrl)
        assertNull(product.id)
    }
}
