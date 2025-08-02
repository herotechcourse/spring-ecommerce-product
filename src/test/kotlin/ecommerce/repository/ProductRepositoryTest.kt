package ecommerce.repository

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductRepositoryTest {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var repository: ProductRepository

    @BeforeEach
    fun setUp() {
        repository = ProductRepository(jdbcTemplate)
    }

    @Test
    fun `insert should return the product with the id`() {
        val original = Product(6, "Espresso", BigDecimal.valueOf(3.00), "url1")
        val product = repository.insert(original)

        assertThat(product.id).isEqualTo(original.id)
    }

    @Test
    fun `get() should return respective product requested with id`() {
        val original = Product(6, "Espresso", BigDecimal.valueOf(3.00), "url1")
        repository.insert(original)

        val product = repository.get(6)
        assertThat(product?.id).isEqualTo(original.id)
        assertThat(product?.name).isEqualTo("Espresso")
    }

    @Test
    fun `updateById should return 1 when product exists and is updated`() {
        val original = Product(1, "Espresso", BigDecimal.valueOf(3.00), "url1")
        repository.insert(original)

        val updated = Product(1, "Cappuccino", BigDecimal.valueOf(4.50), "url2")
        val result = repository.updateById(1, updated)

        assertThat(result).isEqualTo(1)
        val reloaded = repository[1]
        assertThat(reloaded?.name).isEqualTo("Cappuccino")
        assertThat(reloaded?.price).isEqualByComparingTo("4.5")
    }

    @Test
    fun `updateById should return null when product does not exist`() {
        val updated = Product(999, "NoProduct", BigDecimal.valueOf(99.99), "url")
        val result = repository.updateById(999, updated)

        assertThat(result).isNull()
    }

    @Test
    fun `deleteById should return null when product does not exists`() {
        val original = Product(1, "Espresso", BigDecimal.valueOf(3.00), "url1")
        repository.insert(original)
        val result = repository.deleteById(999)
        assertThat(result).isNull()
    }

    @Test
    fun `deleteById should return the deleted product id and delete it from the database`() {
        val original = Product(1, "Espresso", BigDecimal.valueOf(3.00), "url1")
        repository.insert(original)
        assertThat(repository.get(1)).isNotNull()

        val result = repository.deleteById(1)
        assertThat(result).isEqualTo(1)
        assertThat(repository.get(1)).isNull()
    }

    @Test
    fun `count should return correct number of products`() {
        // data.sql already has 3 products

        assertThat(repository.count()).isEqualTo(5)
    }

    @Test
    fun `findByName should return the product with the provided name`() {
        // data.sql already have 3 products
        val result = repository.findByName("Coffee Filter")
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(3)
    }

    @Test
    fun `findByName should return null when product does not exist`() {
        // data.sql already have 3 products
        val result = repository.findByName("Orange Juice")
        assertThat(result).isNull()
    }

    @Test
    fun `isEmptyOrNull should return true when there are no products`() {
        repository.deleteById(1)
        repository.deleteById(2)
        repository.deleteById(3)
        repository.deleteById(4)
        repository.deleteById(5)

        val result = repository.isEmptyOrNull()
        assertThat(result).isTrue()
    }

    @Test
    fun `isEmptyOrNull should return false when there are products in database`() {
        // data.sql already have 3 products
        val result = repository.isEmptyOrNull()
        assertThat(result).isFalse()
    }
}
