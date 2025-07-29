package ecommerce.service

import ecommerce.dao.JdbcCartDAO
import ecommerce.dao.JdbcProductDAO
import ecommerce.exception.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartServiceTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired private lateinit var jdbcProductDAO: JdbcProductDAO

    @Autowired private lateinit var jdbcCartDAO: JdbcCartDAO

    @Autowired private lateinit var cartService: CartService

    @BeforeEach
    fun setUp() {
        jdbcProductDAO = JdbcProductDAO(jdbcTemplate)
        jdbcCartDAO = JdbcCartDAO(jdbcTemplate)
        cartService = CartService(jdbcCartDAO, jdbcProductDAO)

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

        jdbcTemplate.execute("DROP TABLE member CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE member
            (
                id       LONG         NOT NULL AUTO_INCREMENT,
                email    VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            );""",
        )

        jdbcTemplate.execute("DROP TABLE cart_item CASCADE;")
        jdbcTemplate.execute(
            """CREATE TABLE cart_item
            (
                id         LONG NOT NULL AUTO_INCREMENT,
                member_id  LONG NOT NULL,
                product_id LONG NOT NULL,
                quantity   INT DEFAULT 1,
                PRIMARY KEY (id),
    
                FOREIGN KEY (member_id) REFERENCES member (id),
                FOREIGN KEY (product_id) REFERENCES product (id)
            );""",
        )

        val query =
            """INSERT INTO product (name, price, imageUrl) VALUES ('Iron Man', 1000, 'https://alexnsan.comics/imageurl/1');
            INSERT INTO product (name, price, imageUrl) VALUES ('X-men', 1000, 'https://alexnsan.comics/imageurl/2');
            INSERT INTO product (name, price, imageUrl) VALUES ('Superman', 1000, 'https://alexnsan.comics/imageurl/3');
            INSERT INTO product (name, price, imageUrl) VALUES ('Naruto', 1000, 'https://alexnsan.comics/imageurl/4');
            INSERT INTO product (name, price, imageUrl) VALUES ('Full Metal Alchemist', 1000, 'https://alexnsan.comics/imageurl/5');
            
            INSERT INTO member (email, password) VALUES ( 'san@htc.com', 'san1234');
            INSERT INTO member (email, password) VALUES ( 'dan@htc.com', 'dan1234');
            """
        jdbcTemplate.batchUpdate(query)
    }

    @Test
    fun `addToCart() - should throw exception when productId does not exist`() {
        assertThrows<NotFoundException> { cartService.addToCart(MEMBER_ID, INVALID_PRODUCT_ID, 1) }
    }

    companion object {
        const val MEMBER_ID = 1L
        const val INVALID_PRODUCT_ID = 100L
    }
}
