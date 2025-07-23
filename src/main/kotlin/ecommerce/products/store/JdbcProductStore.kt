package ecommerce.products.store

import ecommerce.products.model.Product
import ecommerce.products.model.ProductPatchDTO
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

    override fun patchProduct(
        id: Long,
        patch: ProductPatchDTO,
    ) {
        val updates = mutableListOf<String>()
        val params = mutableListOf<Any>()

        if (patch.name != null) {
            updates.add("name = ?")
            params.add(patch.name)
        }
        if (patch.price != null) {
            updates.add("price = ?")
            params.add(patch.price)
        }
        if (patch.imageUrl != null) {
            updates.add("imageUrl = ?")
            params.add(patch.imageUrl)
        }
        if (updates.isEmpty()) {
            return
        }
        val sql = "UPDATE product SET ${updates.joinToString(", ")} WHERE id = ?"
        params.add(id)
        db.update(sql, *params.toTypedArray())
    }

    override fun deleteProduct(id: Long): Int {
        val deletedCount = db.update("DELETE FROM product WHERE id = ?", id)
        return deletedCount
    }
}
