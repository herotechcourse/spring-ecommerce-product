package ecommerce.dao

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class JdbcProductDAOTest {
    private lateinit var jdbcProductDao: JdbcProductDAO

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcProductDao = JdbcProductDAO(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE product CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE product 
            (
                id          LONG    NOT NULL AUTO_INCREMENT,
                name        varchar(255)    NOT NULL,
                price       DOUBLE  NOT NULL,
                imageUrl    TEXT    NOT NULL,
                PRIMARY KEY (id)
            );""",
        )

        val query = """INSERT INTO product (name, price, imageUrl) VALUES ('Iron Man', 1000, 'https://alexnsan.comics/imageurl/1');
                    INSERT INTO product (name, price, imageUrl) VALUES ('X-men', 1000, 'https://alexnsan.comics/imageurl/2');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Superman', 1000, 'https://alexnsan.comics/imageurl/3');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Naruto', 1000, 'https://alexnsan.comics/imageurl/4');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Full Metal Alchemist', 1000, 'https://alexnsan.comics/imageurl/5');"""
        jdbcTemplate.batchUpdate(query)
    }

    @Test
    fun findAll() {
        val products = jdbcProductDao.findAll()
        assertThat(products).hasSize(5)
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
