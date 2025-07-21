package ecommerce

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val jdbc: JdbcTemplate) {
    private val rowMapper =
        RowMapper<Product> { rs, _ ->
            Product(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                price = rs.getDouble("price"),
                imageUrl = rs.getString("image_url"),
            )
        }

    fun getAll(): List<Product> {
        return jdbc.query("SELECT * FROM products", rowMapper)
    }

    fun create(product: Product): Long {
        jdbc.update(
            "INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)",
            product.name,
            product.price,
            product.imageUrl,
        )
        return jdbc.queryForObject("SELECT MAX(id) FROM products", Long::class.java)!!
    }

    fun update(
        id: Long,
        product: Product,
    ): Boolean {
        return jdbc.update(
            "UPDATE products SET name = ?, price = ?, image_url = ? WHERE id = ?",
            product.name, product.price, product.imageUrl, id,
        ) > 0
    }

    fun delete(id: Long): Boolean {
        return jdbc.update("DELETE FROM products WHERE id = ?", id) > 0
    }

    fun existsById(id: Long): Boolean {
        val sql = "SELECT COUNT(*) FROM products WHERE id = ?"
        val count = jdbc.queryForObject(sql, Int::class.java, id)
        return count != null && count > 0
    }

    fun existsByName(name: String): Boolean {
        val count = jdbc.queryForObject("SELECT COUNT(*) FROM products WHERE name = ?", Long::class.java, name)
        return count != null && count > 0
    }
}
