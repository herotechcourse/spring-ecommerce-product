package ecommerce

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class JdbcProductStoreTest {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var productStore: JdbcProductStore

    @BeforeEach
    fun setUp() {
        productStore = JdbcProductStore(jdbcTemplate)
        // Assume schema.sql and data.sql initialize table and data externally
    }

    @Test
    fun `should return all products`() {
        val products = productStore.findAll()
        assertThat(products).isNotEmpty
        assertThat(products.map { it.name }).contains("Phone", "Laptop")
    }

    @Test
    fun `should find product by id`() {
        val product = productStore.findById(2L)
        assertThat(product).isNotNull
        assertThat(product.name).isEqualTo("Phone")
    }

    @Test
    fun `should save a new product`() {
        val newProduct = Product(name = "Tablet", price = 299.99, imageUrl = "tablet.jpg")
        productStore.save(newProduct) // no return value

        val savedProducts = productStore.findAll()
        assertThat(savedProducts).anyMatch { it.name == "Tablet" && it.price == 299.99 }
    }


    @Test
    fun `should update existing product`() {
        val updatedProduct = Product(name = "Updated Phone", price = 499.99, imageUrl = "updated_phone.jpg")
        productStore.update(1L, updatedProduct)

        val product = productStore.findById(1L)
        assertThat(product.name).isEqualTo("Updated Phone")
        assertThat(product.price).isEqualTo(499.99)
        assertThat(product.imageUrl).isEqualTo("updated_phone.jpg")
    }

    @Test
    fun `should delete a product`() {
        val rowNum = productStore.delete(1L)
        assertThat(rowNum).isEqualTo(1)
    }

}
