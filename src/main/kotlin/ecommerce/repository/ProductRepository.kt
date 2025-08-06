package ecommerce.repository

import ecommerce.model.Product
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.ResultSet

@Repository
class ProductRepository(val jdbcTemplate: JdbcTemplate) : BaseProductStore {
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

    override fun insert(product: Product): Product {
        val sql =
            """
            INSERT INTO products (name, price, image_url)
            VALUES (?, ?, ?)
            """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, product.name)
            ps.setBigDecimal(2, product.price)
            ps.setString(3, product.imageUrl)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve generated id")

        return product.copy(id = generatedId)
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
