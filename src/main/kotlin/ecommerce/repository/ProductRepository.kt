package ecommerce.repository

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
                id = rs.getLong("id"),
                name = rs.getString("product_name"),
                price = rs.getDouble("price"),
                imageUrl = rs.getString("image_url"),
            )
        }

    fun count(): Int {
        val sql = "SELECT COUNT(*) FROM product"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    fun findAll(): List<Product> {
        val sql = "Select * from products"
        return jdbcTemplate.query(sql, productRowMapper)
    }

    fun findById(id: Long): Product? {
        val sql = "Select * from products where id = ?"
        return jdbcTemplate.queryForObject(sql, productRowMapper, id)
    }

    fun save(product: Product) {
        val sql = "insert into products(product_name,price,image_url) values (?,?,?)"
        jdbcTemplate.update(sql, product.name, product.price, product.imageUrl)
    }

    fun update(
        id: Long,
        product: Product,
    ) {
        val sql = "update products set product_name = ?, price = ?, image_url = ? where id = ?"
       jdbcTemplate.update(sql, product.name, product.price, product.imageUrl, id)
    }

    fun deleteById(id: Long) {
        val sql = "delete from products where id = ?"
         jdbcTemplate.update(sql, id)
    }
}
