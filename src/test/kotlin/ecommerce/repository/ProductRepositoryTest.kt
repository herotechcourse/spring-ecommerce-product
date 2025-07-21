package ecommerce.repository

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductRepositoryTest {
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)
        jdbcTemplate.execute("DROP TABLE products IF EXISTS")
        jdbcTemplate.execute(
            """
            CREATE TABLE products (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            product_name VARCHAR(255) NOT NULL,
            price DOUBLE CHECK (price >= 0),
            image_url VARCHAR(255))
            """.trimIndent(),
        )
        jdbcTemplate.update(
            "INSERT INTO products(product_name,price,image_url) VALUES (?,?,?)",
            "Product 1",
            10.2,
            "url.com",
        )
    }

    @Test
    fun findAll() {
        val products = productRepository.findAll()
        assertThat(products.size).isEqualTo(1)
    }

    @Test
    fun findById() {
        val product = productRepository.findById(1)
        assertThat(product?.name).isEqualTo("Product 1")
    }

    @Test
    fun save() {
        productRepository.save(Product(name = "Product 2", price = 10.5, imageUrl = "url.com"))
        assertThat(productRepository.findById(2)).isNotNull()
    }

    @Test
    fun update() {
        productRepository.update(1, Product(name = "Product 2", price = 10.5, imageUrl = "url.com"))
        assertThat(productRepository.findById(1)?.name).isEqualTo("Product 2")
    }

    @Test
    fun deleteById() {
        productRepository.deleteById(1)
        assertThat(productRepository.findAll()).isEmpty()
    }
}
