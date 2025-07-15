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
    fun readProducts() {
        create()
        create()
        val response = controller.getProducts()
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun readProduct() {
        create()
        create()
        val response = controller.getProduct(2)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun update() {
        val product = Product(name = "new product", price = 1.6, imageUrl = "https://www.product.com/image/2")
        create()
        val response = controller.updateProduct(1, product)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
    }
}
