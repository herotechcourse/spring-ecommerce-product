package ecommerce.repository

import ecommerce.model.Product
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.concurrent.atomic.AtomicLong

@Repository
class ProductStore(val jdbcTemplate: JdbcTemplate) : BaseProductStore {
    private val index = AtomicLong(1)

    private val rowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getString("image_url"),
            )
        }

    override fun isEmptyOrNull(): Boolean {
        return count() == 0
    }

    override fun count(): Int? {
        val sql = "SELECT count(*) FROM products"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    override fun findAll(): List<Product> {
        val sql = "SELECT id, name, price, image_url FROM products"
        return jdbcTemplate.query(sql, rowMapper)
    }

    override operator fun get(id: Long): Product? {
        val sql = "SELECT id, name, price, image_url FROM products WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    override fun insert(
        product: Product,
    ): Product {
        val id = index.getAndIncrement()
        val sql =
            """
            INSERT INTO products (id, name, price, image_url)
            VALUES (?, ?, ?, ?)
            """.trimIndent()

        jdbcTemplate.update(sql, id, product.name, product.price, product.imageUrl)
        return product.copy(id = id)
    }

    override fun updateById(
        id: Long,
        product: Product,
    ): Int? {
        val sql =
            """
            UPDATE products 
            SET name = ?, price = ?, image_url = ?
            WHERE id = ?
            """.trimIndent()

        return try {
            jdbcTemplate.update(sql, product.name, product.price, product.imageUrl, id)
                .takeIf { it == 1 }
        } catch (ex: DataAccessException) {
            throw RuntimeException("Failed to update product with id $id", ex)
        }
    }

    override fun findByName(name: String): Product? {
        val sql = "SELECT id, name, price, image_url FROM products WHERE name = ?"
        return jdbcTemplate.query(sql, rowMapper, name).firstOrNull()
    }

    override fun deleteById(id: Long): Int? {
        if (isEmptyOrNull()) return null
        val sql = "DELETE FROM products WHERE id = ?"

        return try {
            jdbcTemplate.update(sql, id).takeIf { it == 1 }
        } catch (ex: DataAccessException) {
            throw RuntimeException("Failed to update product with id $id", ex)
        }
    }
}
