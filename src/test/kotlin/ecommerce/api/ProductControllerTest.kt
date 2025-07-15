package ecommerce.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ProductControllerTest {
    val controller = ProductController()

    @Test
    fun create() {
        val product = Product(name = "product1", price = 1.5, imageUrl = "https://www.product.com/image/1")
        val response = controller.createProduct(product)

        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun read() {
        create()
    }

    @Test
    fun update() {
        create()
    }

    @Test
    fun delete() {
    }
}