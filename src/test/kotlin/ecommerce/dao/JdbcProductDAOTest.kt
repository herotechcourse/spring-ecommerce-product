package ecommerce.dao

import ecommerce.model.Product
import org.assertj.core.api.Assertions
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
            """CREATE TABLE product (
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
        Assertions.assertThat(products).hasSize(5)
    }

    @Test
    fun findById() {
        val product = jdbcProductDao.findById(1)
        Assertions.assertThat(product?.name).isEqualTo("Iron Man")
    }

    @Test
    fun insert() {
        val product = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")
        jdbcProductDao.insert(product)
        val target = jdbcProductDao.findById(6)
        Assertions.assertThat(target?.name).isEqualTo(product.name)
    }

    @Test
    fun update() {
        val id = 1L
        val newProduct = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")
        val product = Product.toEntity(newProduct, id)

        val affectedRow = jdbcProductDao.update(product)
        val target = jdbcProductDao.findById(id)

        Assertions.assertThat(affectedRow).isEqualTo(1)
        Assertions.assertThat(target?.id).isEqualTo(product.id)
        Assertions.assertThat(target?.name).isEqualTo(product.name)
    }

    @Test
    fun delete() {
        val id = 1L
        val result = jdbcProductDao.delete(id)

        Assertions.assertThat(result).isEqualTo(1)
    }
}
