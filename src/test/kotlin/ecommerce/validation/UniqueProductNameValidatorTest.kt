package ecommerce.validation

import ecommerce.model.Product
import ecommerce.repository.ProductStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Qualifier

class UniqueProductNameValidatorTest {
    @Qualifier("jdbcProductStore")
    private lateinit var productStore: ProductStore
    private lateinit var validator: UniqueProductNameValidator

    @BeforeEach
    fun setup() {
        productStore = mock(ProductStore::class.java)
        validator = UniqueProductNameValidator(productStore)
    }

    @Test
    fun `should return true when product name does not exist`() {
        `when`(productStore.findByName("New Product")).thenReturn(null)

        val result = validator.isValid("New Product", null)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when product with same name already exists`() {
        val existingProduct = Product(id = 1L, name = "Existing Product", price = 10.0, imageUrl = "url")
        `when`(productStore.findByName("Existing Product")).thenReturn(existingProduct)

        val result = validator.isValid("Existing Product", null)

        assertThat(result).isFalse()
    }
}
