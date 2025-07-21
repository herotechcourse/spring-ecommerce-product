package ecommerce.repository

import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

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

    fun findAllProducts(): List<Product> {
        val sql = "select id, name, price, img, quantity from products"
        return jdbcTemplate.query(sql, productRowMapper)
    }

    fun create(product: Product) {
        val sql = "insert into products (name, price, img, quantity) values (?, ?, ?, ?)"
        jdbcTemplate.update(sql, product.name, product.price, product.img, product.quantity)
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
