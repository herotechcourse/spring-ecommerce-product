package ecommerce

import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcProductStore(private val jdbcTemplate: JdbcTemplate) : ProductStore {
    private val productRowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                id = rs.getLong("id"),
                name = rs.getString("product_name"),
                price = rs.getDouble("price"),
                imageUrl = rs.getString("image_url"),
            )
        }

    override fun countProducts(): Int {
        val sql = "SELECT COUNT(*) FROM products"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    override fun findAll(): List<Product> {
        val sql = "Select * from products"
        return jdbcTemplate.query(sql, productRowMapper)
    }

    override fun findById(id: Long): Product {
        val sql = "SELECT * FROM products WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, productRowMapper, id)
            ?: throw NotFoundException("Product with id $id not found")
    }

    override fun save(product: Product) {
        val sql = "insert into products(product_name,price,image_url) values (?,?,?)"
        jdbcTemplate.update(sql, product.name, product.price, product.imageUrl)
    }

    override fun update(
        id: Long,
        product: Product,
    ): Int {
        val sql = "update products set product_name = ?, price = ?, image_url = ? where id = ?"
        return jdbcTemplate.update(sql, product.name, product.price, product.imageUrl, id)
    }

    override fun delete(id: Long): Int {
        val sql = "delete from products where id = ?"
        return jdbcTemplate.update(sql, id)
    }
}
