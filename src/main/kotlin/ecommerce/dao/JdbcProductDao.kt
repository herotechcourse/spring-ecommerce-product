package ecommerce.dao

import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcProductDao(private val db: JdbcTemplate) : ProductDao {
    private val productRowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("imageUrl"),
            )
        }

    override fun findAll(): List<Product> {
        val products = db.query("SELECT * FROM product;", productRowMapper)
        return products
    }

    override fun findById(id: Long): Product? {
        try {
            return db.queryForObject(
                "SELECT id, name, price, imageUrl FROM product WHERE id = ?",
                productRowMapper,
                id,
            )
        } catch (exception: Exception) {
            println("findById(): " + exception.message)
            return null
        }
    }

    override fun insert(product: Product): Long {
        val sql = "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            connection.prepareStatement(sql, arrayOf("id")).apply {
                setString(1, product.name)
                setDouble(2, product.price)
                setString(3, product.imageUrl)
            }
        }, keyHolder)
        return keyHolder.key?.toLong() ?: throw IllegalStateException(MESSAGE_INSERT_RETRIEVE_ID_FAILED)
    }

    override fun update(product: Product): Int {
        val value =
            db.update(
                "UPDATE product SET name = ?, price = ?, imageUrl = ? WHERE id = ?",
                product.name,
                product.price,
                product.imageUrl,
                product.id,
            )
        return value
    }

    override fun delete(id: Long): Int {
        val value = db.update("DELETE FROM product WHERE id = ?", id)
        return value
    }

    override fun existsByName(name: String): Boolean {
        val sql = "SELECT COUNT(*) FROM product WHERE name = ?"
        val count = db.queryForObject(sql, Long::class.java, name)
        return count != null && count > 0
    }

    companion object {
        const val MESSAGE_INSERT_RETRIEVE_ID_FAILED = "insert - Failed to retrieve ID"
    }
}
