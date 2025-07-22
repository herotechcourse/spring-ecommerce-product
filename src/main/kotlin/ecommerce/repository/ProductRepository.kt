package ecommerce.repository

import ecommerce.dto.ProductDTO
import ecommerce.mapper.ProductRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val productRowMapper: ProductRowMapper,
) {
    fun findAll(): List<ProductDTO> {
        val sql = "select * from products"
        return jdbcTemplate.query(sql, productRowMapper)
    }

    fun findById(id: Long): ProductDTO? {
        val sql = "select * from products where id = ?"
        return jdbcTemplate.queryForObject(sql, productRowMapper, id)
    }

    fun create(product: ProductDTO): Long  {
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("products")
                .usingGeneratedKeyColumns("id")

        val parameters =
            mapOf(
                "name" to product.name,
                "description" to product.description,
                "price" to product.price,
                "image_url" to product.imageUrl,
                "quantity" to product.quantity,
            )
        return insert.executeAndReturnKey(parameters).toLong()
    }

    fun update(
        id: Long,
        product: ProductDTO,
    ) {
        findById(id)
        val sql = "update products set name = ?, price = ?, image_url = ? where id = ?"
        jdbcTemplate.update(sql, product.name, product.price, product.imageUrl, id)
    }

    fun deleteById(id: Long) {
        findById(id)
        val sql = "delete from products where id = ?"
        jdbcTemplate.update(sql, id)
    }
}
