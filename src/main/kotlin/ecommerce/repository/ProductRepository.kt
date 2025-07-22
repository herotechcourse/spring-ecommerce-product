package ecommerce.repository

import ecommerce.dto.ProductRequest
import ecommerce.model.Product
import org.springframework.dao.EmptyResultDataAccessException
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
                rs.getString("image_url"),
            )
        }

    fun findById(id: Long): Product? {
        return try {
            jdbcTemplate.queryForObject("select id, name, price, image_url from products where id = ?", productRowMapper, id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun getAll(): List<Product> {
        return jdbcTemplate.query("select id, name, price, image_url from products", productRowMapper)
    }

    fun createProduct(product: ProductRequest): Boolean {
        return jdbcTemplate.update(
            "insert into products (name, price, image_url) values (?,?,?)",
            product.name,
            product.price,
            product.imageUrl,
        ) > 0
    }

    fun existsById(id: Long): Boolean {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products WHERE id =?", Boolean::class.java, id) ?: false
    }

    fun updateProduct(
        id: Long,
        product: ProductRequest,
    ): Boolean {
        return jdbcTemplate.update(
            "UPDATE products SET name=?, price= ?, image_url = ? where id = ?",
            product.name,
            product.price,
            product.imageUrl,
            id,
        ) > 0
    }

    fun deleteProduct(id: Long): Boolean {
        return jdbcTemplate.update("delete from products where id = ?", id.toLong()) > 0
    }
}
