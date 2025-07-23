package ecommerce.product.helper

import ecommerce.product.data.ProductRequest
import ecommerce.product.data.ProductResponse
import jakarta.validation.ConstraintViolation
import org.assertj.core.api.Assertions.assertThat

object CustomAssertExtension {
    fun ProductResponse.shouldEquals(
        expected: ProductRequest,
        expectedId: Long,
    ) {
        assertThat(this.id).isEqualTo(expectedId)
        assertThat(this.name).isEqualTo(expected.name)
        assertThat(this.price).isEqualTo(expected.price.toPlainString())
        assertThat(this.imageUrl).isEqualTo(expected.imageUrl)
    }

    fun Set<ConstraintViolation<*>>.shouldContainViolation(
        field: String,
        expectedMessage: String,
    ) {
        assertThat(this).anyMatch {
            it.propertyPath.toString() == field && it.message == expectedMessage
        }
    }
}
