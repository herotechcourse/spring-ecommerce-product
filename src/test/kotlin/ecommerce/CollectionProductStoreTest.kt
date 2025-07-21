package ecommerce

import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CollectionProductStoreTest {
    private lateinit var productStore: CollectionProductStore

    @BeforeEach
    fun setUp() {
        val products =
            mutableListOf(
                Product(id = 1L, name = "Phone", price = 500.0, imageUrl = "phone.jpg"),
                Product(id = 2L, name = "Laptop", price = 1500.0, imageUrl = "laptop.jpg"),
            )
        productStore = CollectionProductStore(products)
    }

    @Test
    fun `should return correct count of products`() {
        val count = productStore.countProducts()
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `should return all products`() {
        val all = productStore.findAll()
        assertThat(all).hasSize(2)
    }

    @Test
    fun `should return product by id`() {
        val product = productStore.findById(1L)
        assertThat(product?.name).isEqualTo("Phone")
    }

    @Test
    fun `should throw exception when product not found`() {
        assertThatThrownBy { productStore.findById(99L) }
            .isInstanceOf(NotFoundException::class.java)
            .hasMessageContaining("Product with id 99 not found")
    }

    @Test
    fun `should save a new product`() {
        val newProduct = Product(id = 3L, name = "Tablet", price = 300.0, imageUrl = "tablet.jpg")
        productStore.save(newProduct)

        val saved = productStore.findById(3L)
        assertThat(saved?.name).isEqualTo("Tablet")
        assertThat(productStore.countProducts()).isEqualTo(3)
    }

    @Test
    fun `should update existing product`() {
        val updated = Product(id = 1L, name = "Smartphone", price = 600.0, imageUrl = "smartphone.jpg")
        productStore.update(1L, updated)

        val product = productStore.findById(1L)
        assertThat(product?.name).isEqualTo("Smartphone")
        assertThat(product?.price).isEqualTo(600.0)
    }

    @Test
    fun `should delete a product`() {
        productStore.delete(2L)

        assertThat(productStore.countProducts()).isEqualTo(1)
        assertThatThrownBy { productStore.findById(2L) }
            .isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `should throw exception when deleting non-existent product`() {
        assertThatThrownBy { productStore.delete(99L) }
            .isInstanceOf(NotFoundException::class.java)
    }
}
