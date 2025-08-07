package ecommerce.unit.service

import ecommerce.dto.product.ProductRequest
import ecommerce.exception.ProductValidationException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import ecommerce.service.ProductService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.dao.DataIntegrityViolationException

class ProductServiceTest {
    private val productRepository = mock<ProductRepository>()
    private val productService = ProductService(productRepository)

    @Test
    fun `should throw ProductValidationException when product name already exists`() {
        val request = ProductRequest("test name88", 1.0, "http://example.com")
        val expectedProduct = Product(name = "test name88", price = 1.0, imageUrl = "http://example.com")

        `when`(productRepository.save(expectedProduct))
            .thenThrow(DataIntegrityViolationException("Unique constraint violation"))

        assertThrows<ProductValidationException> {
            productService.createProduct(request)
        }
    }
}
