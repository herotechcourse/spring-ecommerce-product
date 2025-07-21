package ecommerce.api

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ProductService(private val db: JdbcTemplate) {
    private val productRowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("imageUrl"),
            )
        }

    fun findAll(): List<Product> {
        val products = db.query("SELECT * FROM product;", productRowMapper)
        return products
    }

    fun findById(id: Long): Product? {
        var product: Product?
        try {
            product =
                db.queryForObject(
                    "SELECT id, name, price, imageUrl FROM product WHERE id = ?",
                    productRowMapper,
                    id,
                )
        } catch (exception: Exception) {
            println(exception.message)
            return null
        }
        return product
    }

    fun insert(product: Product) {
        db.update(
            "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?);",
            product.name,
            product.price,
            product.imageUrl,
        )
    }

    fun update(
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

    fun delete(id: Long): Int {
        val value = db.update("DELETE FROM product WHERE id = ?", id)
        return value
    }
}
