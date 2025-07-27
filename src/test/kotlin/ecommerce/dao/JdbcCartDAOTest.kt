package ecommerce.dao

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class JdbcCartDAOTest {
    private lateinit var jdbcCartDAO: JdbcCartDAO

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcCartDAO = JdbcCartDAO(jdbcTemplate)

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
    fun addItemToCart() {
        val itemId = jdbcCartDAO.addItemToCart(MEMBER_ID, PRODUCT_ID)
        assertThat(itemId).isNotNull()
        val cartItems = jdbcCartDAO.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems.first().quantity).isEqualTo(1)
    }

    @Test
    fun `addItemToCart() - update quantity when the product already exists in the cart`() {
        addItemToCart()
        val itemId = jdbcCartDAO.addItemToCart(MEMBER_ID, PRODUCT_ID)
        assertThat(itemId).isNotNull()
        val cartItems = jdbcCartDAO.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems.first().quantity).isEqualTo(2)
    }

    @Test
    fun getCartItemsByMemberId() {
        addItemToCart()
        val cartItems = jdbcCartDAO.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems).hasSize(1)
    }

    @Test
    fun removeItemFromCart() {
        addItemToCart()
        assertThat(jdbcCartDAO.removeItemFromCart(MEMBER_ID, PRODUCT_ID)).isEqualTo(1)
        assertThat(jdbcCartDAO.getCartItemsByMemberId(MEMBER_ID)).hasSize(0)
    }

    @Test
    fun updateItemQuantityInCart() {
        addItemToCart()
        assertThat(jdbcCartDAO.updateItemQuantityInCart(MEMBER_ID, PRODUCT_ID, QUANTITY)).isEqualTo(1)
        val cartItems = jdbcCartDAO.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems.first().quantity).isEqualTo(QUANTITY)
    }

    companion object {
        const val MEMBER_ID = 1L
        const val PRODUCT_ID = 1L
        const val QUANTITY = 10
    }
}
