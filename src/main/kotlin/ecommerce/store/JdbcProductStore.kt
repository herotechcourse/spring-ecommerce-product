package ecommerce.store

import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcProductStore(private val db: JdbcTemplate) : ProductStore {
    private val productRowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("imageUrl"),
            )
        }

    override fun findAllProducts(): List<Product> {
        return db.query("SELECT * FROM product", productRowMapper)
    }

    override fun findProductById(id: Long): Product? {
        return try {
            db.queryForObject("SELECT * FROM product WHERE id = ?", productRowMapper, id)
        } catch (e: Exception) {
            null
        }
    }

    override fun insertProduct(product: Product): Product {
        val keyHolder = GeneratedKeyHolder()
        db.update({ connection ->
            connection.prepareStatement("INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)", arrayOf("id")).apply {
                setString(1, product.name)
                setDouble(2, product.price)
                setString(3, product.imageUrl)
            }
        }, keyHolder)
        return product.copy(id = keyHolder.key?.toLong() ?: throw IllegalStateException("No ID returned"))
    }

    override fun updateProduct(
        id: Long,
        product: Product,
    ) {
        db.update(
            "UPDATE product SET name = ?, price = ?, imageUrl = ? WHERE id = ?",
            product.name,
            product.price,
            product.imageUrl,
            id,
        )
    }

    override fun deleteProduct(id: Long): Int {
        val deletedCount = db.update("DELETE FROM product WHERE id = ?", id)
        return deletedCount
    }
}
