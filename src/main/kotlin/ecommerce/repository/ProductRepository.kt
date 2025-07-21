package ecommerce.repository

import ecommerce.dto.ProductRequest
import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
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

    fun findById(id: Long): Product? {
        return jdbcTemplate.queryForObject("select id, name, price, image_url from products where id = ?", productRowMapper, id)
    }

    fun getAll(): List<Product> {
        return jdbcTemplate.query("select id, name, price, image_url from products", productRowMapper)
    }

    fun createProduct(product: ProductRequest) {
        jdbcTemplate.update(
            "insert into products (name, price, image_url) values (?,?,?)",
            product.name,
            product.price,
            product.imageUrl,
        )
    }

    fun updateProduct(product: Product) {
        jdbcTemplate.update(
            "UPDATE products SET name=?, price= ?, image_url = ? where id = ?",
            product.name,
            product.price,
            product.imageUrl,
            product.id,
        )
    }

    fun deleteProduct(id: Long) {
        jdbcTemplate.update("delete from products where id = ?", id.toLong())
    }
}
