package ecommerce.service

import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
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
        val products = db.query("""select * from product;""", productRowMapper)
        return products
    }

    fun findById(id: Long): Product? {
        var product: Product?
        try {
            product =
                db.queryForObject(
                    """select id, name, price, imageUrl from product where id = ?""",
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
            """INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?);""",
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
            """UPDATE product set name = ?, price = ?, imageUrl = ? WHERE id = ?""",
            product.name,
            product.price,
            product.imageUrl,
            id,
        )
    }

    fun delete(id: Long): Int {
        val value = db.update("""DELETE FROM product WHERE id = ?""", id)
        return value
    }
}
