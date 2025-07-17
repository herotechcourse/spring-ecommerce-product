package ecommerce.repository

import ecommerce.product.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ProductRepository(private val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("image_url"),
            )
        }

    fun isEmptyOrNull(): Boolean {
        return count() == 0
    }

    fun count(): Int? {
        val sql = "select count(*) from products"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    fun findAll(): List<Product> {
        val sql = "SELECT id, name, price, image_url FROM products"
        return jdbcTemplate.query(sql, rowMapper)
    }

    operator fun get(id: Long): Product? {
        val sql = "SELECT id, name, price, image_url FROM products WHERE id = ?"
        return try {
            jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (exception: Exception) {
            null
        }
    }

    fun insert(
        id: Long,
        product: Product,
    ): Int? {
        val sql =
            """
            INSERT INTO products (id, name, price, image_url)
            VALUES (?, ?, ?, ?)
            """.trimIndent()

        return jdbcTemplate.update(sql, id, product.name, product.price, product.imageUrl)
    }

    fun updateById(
        id: Long,
        product: Product,
    ): Int? {
        val sql =
            """
            UPDATE products 
            SET name = ?, price = ?, image_url = ?
            WHERE id = ?
            """.trimIndent()

        return jdbcTemplate.update(sql, product.name, product.price, product.imageUrl, id).takeIf { it == 1 }
    }

    fun deleteById(id: Long): Int? {
        if (isEmptyOrNull()) return null
        val sql = "DELETE FROM products WHERE id = ?"
        return jdbcTemplate.update(sql, id).takeIf { it == 1 }
    }
}