package ecommerce.api.cart

import ecommerce.cart.store.JdbcCartStore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql

@JdbcTest
@Sql(scripts = ["/schema.sql"])
class JdbcCartStoreTest
    @Autowired
    constructor(
        val jdbcTemplate: JdbcTemplate,
    ) {
        private val cartStore = JdbcCartStore(jdbcTemplate)

        @Test
        fun `should create a cart and associate to user`() {
            jdbcTemplate.update("INSERT INTO member (email, password) VALUES (?, ?)", "a@gmail.com", "password")
            val memberId =
                jdbcTemplate.queryForObject("SELECT id FROM member WHERE email = 'a@gmail.com'", Long::class.java)

            val cart = cartStore.createCart(memberId!!)
            val found = cartStore.findCartByMemberId(memberId)
            assertNotNull(found)
            assertEquals(memberId, found!!.memberId)
            assertTrue(found.items.isEmpty())
        }

        @Test
        fun `should add item and get it`() {
            jdbcTemplate.update("INSERT INTO member (email, password) VALUES (?, ?)", "b@gmail.com", "password")
            val memberId =
                jdbcTemplate.queryForObject("SELECT id FROM member WHERE email = 'b@gmail.com'", Long::class.java)
            jdbcTemplate.update(
                "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)",
                "prod1",
                10.0,
                "http://img.com/img1",
            )
            val productId = jdbcTemplate.queryForObject("SELECT id FROM product WHERE name = ?", Long::class.java, "prod1")
            val cart = cartStore.createCart(memberId!!)

            cartStore.addCartItem(cart.id, productId!!, 2)

            val items = cartStore.getItems(cart.id)
            assertEquals(1, items.size)
            assertEquals(2, items[0].quantity)
        }

        @Test
        fun `should add item and delete it`() {
            jdbcTemplate.update("INSERT INTO member (email, password) VALUES (?, ?)", "c@gmail.com", "password")
            val memberId =
                jdbcTemplate.queryForObject("SELECT id FROM member WHERE email = 'c@gmail.com'", Long::class.java)
            jdbcTemplate.update(
                "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)",
                "prod2",
                10.0,
                "http://img.com/img1",
            )
            val productId = jdbcTemplate.queryForObject("SELECT id FROM product WHERE name = ?", Long::class.java, "prod2")
            val cart = cartStore.createCart(memberId!!)

            cartStore.addCartItem(cart.id, productId!!, 1)

            val items = cartStore.getItems(cart.id)
            assertEquals(1, items.size)

            cartStore.removeCartItem(cart.id, productId)
            val itemsUpdated = cartStore.getItems(cart.id)
            assertEquals(0, itemsUpdated.size)
        }

        @Test
        fun `should add items and clear the cart`() {
            jdbcTemplate.update("INSERT INTO member (email, password) VALUES (?, ?)", "d@gmail.com", "password")
            val memberId =
                jdbcTemplate.queryForObject("SELECT id FROM member WHERE email = 'd@gmail.com'", Long::class.java)
            jdbcTemplate.update(
                "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)",
                "prod5",
                10.0,
                "http://img.com/img1",
            )
            val productId = jdbcTemplate.queryForObject("SELECT id FROM product WHERE name = ?", Long::class.java, "prod5")
            jdbcTemplate.update(
                "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)",
                "prod6",
                10.0,
                "http://img.com/img1",
            )
            val product2Id = jdbcTemplate.queryForObject("SELECT id FROM product WHERE name = ?", Long::class.java, "prod6")
            val cart = cartStore.createCart(memberId!!)

            cartStore.addCartItem(cart.id, productId!!, 1)
            cartStore.addCartItem(cart.id, product2Id!!, 1)

            val items = cartStore.getItems(cart.id)
            assertEquals(2, items.size)

            cartStore.clearCart(cart.id)
            val itemsUpdated = cartStore.getItems(cart.id)
            assertEquals(0, itemsUpdated.size)
        }
    }
