package ecommerce.repository

import ecommerce.domain.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement

@Repository
class ProductRepository(private val jdbcTemplate: JdbcTemplate) {
    private val productRowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("img"),
                rs.getInt("quantity"),
            )
        }

    fun count(): Int {
        val sql = "select count(*) from products"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    fun findById(id: Long): Product? {
        val sql = "select * from products where id = ?"
        return jdbcTemplate.query(sql, productRowMapper, id).firstOrNull()
    }


    fun findByName(name: String): Product? {
        val sql = "select * from products where name = ?"
        return jdbcTemplate.query(sql, productRowMapper, name).toList().firstOrNull()
    }

    fun findAllProducts(): List<Product> {
        val sql = "select id, name, price, img, quantity from products"
        return jdbcTemplate.query(sql, productRowMapper)
    }


    fun create(product: Product): Product {
        val sql = "insert into products (name, price, img, quantity) values (?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, product.name)
            ps.setDouble(2, product.price)
            ps.setString(3, product.img)
            ps.setInt(4, product.quantity)
            ps
        }, keyHolder)

        product.id = keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve generated ID for product.")

        return product

    }

    fun update(
        id: Long,
        product: Product,
    ) {
        val sql = "update products set name = ?, price = ?, img = ?, quantity = ? where id = ?"
        jdbcTemplate.update(sql, product.name, product.price, product.img, product.quantity, id)
    }

    fun delete(id: Long) {
        val sql = "delete from products where id = ?"
        jdbcTemplate.update(sql, id)
    }
}
