package ecommerce.unit.service

import ecommerce.dto.product.ProductRequest
import ecommerce.exception.ProductValidationException
import ecommerce.repository.ProductRepository
import ecommerce.service.ProductService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ProductServiceTest {
    private val productRepository = mock(ProductRepository::class.java)
    private val productService = ProductService(productRepository)

    @Test
    fun `should throw ProductValidationException when product name already exists`() {
        // Given
        val request =
            ProductRequest(
                name = "duplicate name",
                price = 10.0,
                imageUrl = "http://example.com",
            )
        `when`(productRepository.existsByName("duplicate name")).thenReturn(true)

        // When & Then
        assertThrows<ProductValidationException> {
            productService.createProduct(request)
        }
    }
}
