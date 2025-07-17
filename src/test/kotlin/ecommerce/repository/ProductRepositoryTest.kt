package ecommerce.repository

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class ProductRepositoryTest {
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE products IF EXISTS")

        jdbcTemplate.execute(createQuery())

        jdbcTemplate.execute(createData())
    }

    @Test
    fun getAll() {
        val products = productRepository.getAll()
        assertThat(products).hasSize(4)
    }

    @Test
    fun update() {
        val product = Product(1, "update", 11.00, "test.com")
        val products = productRepository.updateProduct(product)
        assertThat(product.name).isEqualTo("update")
    }

    private fun createQuery(): String {
        return """
            create table PRODUCTS
            (
                ID       int              not null AUTO_INCREMENT,
                NAME     varchar(100)     not null,
                PRICE    double not null,
                IMAGE_URL varchar(500),
                PRIMARY KEY (ID)
            )
            """.trimIndent()
    }

    fun createData(): String {
        return """
            INSERT INTO PRODUCTS (name, price, image_url)
            VALUES
            ('Coffee', 10.99, 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Roasted_coffee_beans.jpg/1200px-Roasted_coffee_beans.jpg'),
            ('Hand cream', 6.99, 'https://cdn.idealo.com/folder/Product/6178/3/6178302/s1_produktbild_gross/neutrogena-hydro-boost-creme-gel-50ml.jpg'),
            ('Chair', 77.99, 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRqUHblIQ5QCehPdDXFHqSqUKqxNExCjw_mKe-wsYJYNXP7S7pnlrGpBKEPGzutl3CSIxRQSJjb3HbbQevqNeDMZQdxtP0ml591k0zsn-b-4KAMs9aMwJngvHNPGf7KHbNhUuOn6g&usqp=CAc'),
            ('Notebook', 3.49, 'https://upload.wikimedia.org/wikipedia/commons/6/6e/Moleskine_notebook.jpg');
            """.trimIndent()
    }
}
