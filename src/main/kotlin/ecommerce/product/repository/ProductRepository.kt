package ecommerce.product.repository

import ecommerce.product.domain.Product
import jakarta.validation.ValidationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
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

    fun findAllProducts(): List<Product> {
        val sql = "SELECT id, name, price, image_url FROM products"
        val products: List<Product> = jdbcTemplate.query(sql, productRowMapper)
        return products
    }

    fun existsByName(
        name: String,
        excludeId: Long? = null,
    ): Boolean {
        val sql = """
            SELECT EXISTS (
                SELECT 1 FROM PRODUCTS WHERE name = ? AND (? IS NULL OR id != ?)
            )
        """
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, name, excludeId, excludeId) ?: false
    }

    fun insert(product: Product) {
        if (existsByName(product.name)) {
            throw ValidationException("Product name must be unique")
        }
        val jdbcInsert = SimpleJdbcInsert(jdbcTemplate).withTableName("products").usingGeneratedKeyColumns("id")
        val parameters =
            mapOf(
                "name" to product.name,
                "price" to product.price,
                "image_url" to product.imageUrl,
            )
        val id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        product.id = id
    }

    fun edit(
        product: Product,
        productId: Long,
    ) {
        if (existsByName(product.name, productId)) {
            throw ValidationException("Product name must be unique")
        }
        val sql = "UPDATE products SET name = ?, price = ?, image_url = ? WHERE id = ?"
        jdbcTemplate.update(sql, product.name, product.price, product.imageUrl, productId)
    }

    fun delete(id: Long): Int {
        return jdbcTemplate.update("DELETE FROM products WHERE id = ?", id)
    }

    fun findById(id: Long): Product? {
        val sql = "SELECT * FROM PRODUCTS WHERE id = ?"
        return jdbcTemplate.query(sql, { rs, _ ->
            Product(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                price = rs.getDouble("price"),
                imageUrl = rs.getString("image_url"),
            )
        }, id).firstOrNull()
    }
}
