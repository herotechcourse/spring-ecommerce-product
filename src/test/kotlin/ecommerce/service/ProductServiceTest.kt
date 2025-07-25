package ecommerce.service

import ecommerce.dao.JdbcProductDAO
import ecommerce.dto.ProductForm
import ecommerce.exception.ProductNameAlreadyExistsException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductServiceTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var jdbcProductDao: JdbcProductDAO
    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        jdbcProductDao = JdbcProductDAO(jdbcTemplate)
        productService = ProductService(jdbcProductDao)

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

        val query =
            """INSERT INTO product (name, price, imageUrl) VALUES ('Iron Man', 1000, 'https://alexnsan.comics/imageurl/1');
                    INSERT INTO product (name, price, imageUrl) VALUES ('X-men', 1000, 'https://alexnsan.comics/imageurl/2');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Superman', 1000, 'https://alexnsan.comics/imageurl/3');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Naruto', 1000, 'https://alexnsan.comics/imageurl/4');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Full Metal Alchemist', 1000, 'https://alexnsan.comics/imageurl/5');"""
        jdbcTemplate.batchUpdate(query)
    }

    @Test
    fun `insert() - should return 400 when name of product already exists`() {
        val productForm = ProductForm(name = "Iron Man", price = 1.5, imageUrl = "https://www.product.com/image/1")
        assertThrows<ProductNameAlreadyExistsException> { productService.insert(productForm) }
    }

    @Test
    fun `insert() - should return 201 when name of product not exists`() {
        val productForm = ProductForm(name = "Iron Body", price = 1.5, imageUrl = "https://www.product.com/image/1")
        val response = productService.insert(productForm)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.CREATED.value())
    }
}
