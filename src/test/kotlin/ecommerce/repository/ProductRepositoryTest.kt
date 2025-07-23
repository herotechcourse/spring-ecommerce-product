package ecommerce.repository

import ecommerce.product.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal

@JdbcTest
class ProductRepositoryTest {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var repository: ProductRepository

    @BeforeEach
    fun setUp() {
        repository = ProductRepository(jdbcTemplate)
    }

    @Test
    fun `updateById should return 1 when product exists and is updated`() {
        val original = Product(1, "Espresso", BigDecimal.valueOf(3.00), "url1")
        repository.insert(1, original)

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
        repository.insert(1, original)
        val result = repository.deleteById(999)
        assertThat(result).isNull()
    }
}
