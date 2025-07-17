package ecommerce

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Repository
class ProductRepository(private val jdbcTemplate: JdbcTemplate) {
    private val productRowMapper = RowMapper<Product> { rs: ResultSet, _ ->
        Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDouble("price"),
            rs.getString("image_url"),
        )
    }

    fun findAllProducts(): List<Product> {
        val sql = "select id, name, price, image_url from products"
        val products: List<Product> = jdbcTemplate.query(sql, productRowMapper)
        return products
    }

    fun insert(product: Product) {
        val sql = "insert into products (name, price, image_url) values (?, ?, ?)"
        jdbcTemplate.update(sql, product.name, product.price, product.imageUrl)
    }

    fun findProductById(id: Long): Product? {
        val product = jdbcTemplate.queryForObject(
            "select id, name, price, image_url from products where id = ?",
            productRowMapper,
            id)
        return product
    }

    fun delete(id: Long): Int {
        return jdbcTemplate.update("delete from products where id = ?", id)
    }

}
