package ecommerce.dao

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/product.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class JdbcProductDaoTest(
    @Autowired private val jdbcProductDao: JdbcProductDao,
) {
    @Test
    fun findAll() {
        val products = jdbcProductDao.findAll()
        assertThat(products).hasSize(6)
    }

    @Test
    fun findById() {
        val product = jdbcProductDao.findById(1)
        assertThat(product?.name).isEqualTo("Iron Man")
    }

    @Test
    fun insert() {
        val product = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")
        val id = jdbcProductDao.insert(product)
        val target = jdbcProductDao.findById(id)
        assertThat(target?.name).isEqualTo(product.name)
    }

    @Test
    fun update() {
        val id = 1L
        val newProduct = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")
        val product = Product.toEntity(newProduct, id)

        val affectedRow = jdbcProductDao.update(product)
        val target = jdbcProductDao.findById(id)

        assertThat(affectedRow).isEqualTo(1)
        assertThat(target?.id).isEqualTo(product.id)
        assertThat(target?.name).isEqualTo(product.name)
    }

    @Test
    fun delete() {
        val id = 1L
        val result = jdbcProductDao.delete(id)

        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `existsByName() - return true if a product with same name exists`() {
        val target = jdbcProductDao.existsByName("Iron Man")

        assertThat(target).isTrue()
    }

    @Test
    fun `existsByName() - return false if a product with same name does not exist`() {
        val target = jdbcProductDao.existsByName("Iron Body")

        assertThat(target).isFalse()
    }
}
