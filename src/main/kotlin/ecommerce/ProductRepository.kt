package ecommerce

import jakarta.validation.ValidationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
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
        val sql = "SELECT id, name, price, image_url FROM products"
        val products: List<Product> = jdbcTemplate.query(sql, productRowMapper)
        return products
    }

    fun existsByName(name: String, excludeId: Long? = null): Boolean {
        val sql = "SELECT COUNT(*) FROM products WHERE name = ? AND (? IS NULL OR id != ?)"
        val count = jdbcTemplate.queryForObject(sql, Int::class.java, name, excludeId, excludeId) ?: 0
        return count > 0
    }

    fun insert(product: Product) {
        if (existsByName(product.name)) {
            throw ValidationException("Product name must be unique")
        }
        val jdbcInsert = SimpleJdbcInsert(jdbcTemplate)
            .withTableName("products")
            .usingGeneratedKeyColumns("id")
        val parameters = mapOf(
            "name" to product.name,
            "price" to product.price,
            "image_url" to product.imageUrl
        )
        val id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        product.id = id
//        if (existsByName(product.name)) {
//            throw ValidationException("Product name must be unique")
//        }
//        val sql = "INSERT INTO products (name, price, image_url) VALUES (?, ?, ?) RETURNING id"
//        val id = jdbcTemplate.queryForObject(sql, Long::class.java, product.name, product.price, product.imageUrl)
//        product.id = id
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
}
