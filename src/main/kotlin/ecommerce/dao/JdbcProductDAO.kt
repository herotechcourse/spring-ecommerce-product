package ecommerce.dao

import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcProductDAO(private val db: JdbcTemplate) : ProductDAO {
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
            println(exception.message)
            return null
        }
    }

    override fun insert(product: Product) {
        db.update(
            "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?);",
            product.name,
            product.price,
            product.imageUrl,
        )
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
}
