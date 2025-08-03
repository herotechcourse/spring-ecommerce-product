package ecommerce.entity

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ProductTest {
    @Test
    fun `should create a product`() {
        val product =
            Product(
                name = "Vanilla Ice Cream",
                price = Price(1.99),
                imageUrl = "https://example.com/vanilla.jpg",
            )

        Assertions.assertEquals("Vanilla Ice Cream", product.name)
        Assertions.assertEquals(Price(1.99), product.price)
        Assertions.assertEquals("https://example.com/vanilla.jpg", product.imageUrl)
        Assertions.assertNull(product.id)
    }
}
