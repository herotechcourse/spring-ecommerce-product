package ecommerce.repository

import ecommerce.domain.NewProduct
import ecommerce.dto.mapper.ProductMapper
import ecommerce.entity.Product
import ecommerce.helper.JdbcHelper
import ecommerce.sql.ProductConstsSQL
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

@Repository
class ProductRepository(private val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<Product> { rs: ResultSet, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getString("image_url"),
            )
        }

    fun findAll(): List<Product> {
        val sql = ProductConstsSQL.SELECT_ALL
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: Long): Product? {
        val sql = ProductConstsSQL.SELECT_BY_ID
        val result = jdbcTemplate.query(sql, arrayOf(id), rowMapper)

        return when (result.size) {
            0 -> null
            1 -> result.first()
            else -> {
                throw IllegalStateException(
                    "Data integrity violation: found ${result.size} products with same id $id. " +
                        "This should never happen with a proper primary key constraint.",
                )
            }
        }
    }

    fun putById(
        id: Long,
        newProduct: NewProduct,
    ): Product? {
        val sql = ProductConstsSQL.UPDATE_BY_ID.trimIndent()
        val entity = ProductMapper.toEntity(newProduct, id)!!
        val affected = jdbcTemplate.update(sql, entity.name, entity.price, entity.imageUrl, entity.id)
        return when {
            affected > 0 -> entity
            else -> null
        }
    }

    fun deleteById(id: Long) {
        val sql = ProductConstsSQL.DELETE_BY_ID.trimIndent()
        jdbcTemplate.update(sql, id)
    }

    fun existsById(id: Long): Boolean {
        val sql = ProductConstsSQL.COUNT_BY_ID
        val found = jdbcTemplate.queryForObject(sql, Int::class.java, id)
        return found != null && found > 0
    }

    fun existsByName(name: String): Boolean {
        val sql = ProductConstsSQL.EXISTS_BY_NAME
        val existing = jdbcTemplate.queryForObject(sql, Boolean::class.java, name)
        return existing ?: false
    }

    fun insertWithKeyholder(newProduct: NewProduct): Long {
        val sql = ProductConstsSQL.INSERT.trimIndent()
        val id = JdbcHelper.insertAndReturnKey(jdbcTemplate, sql, newProduct, ::prepareInsertStatement)

        return requireNotNull(id) {
            "Failed to retrieve generated ID after inserting product '${newProduct.name}'. " +
                "Database key generation failed."
        }
    }

    private fun prepareInsertStatement(
        connection: Connection,
        sql: String,
        newProduct: NewProduct,
    ): PreparedStatement {
        return connection.prepareStatement(sql, arrayOf("id")).apply {
            setString(1, newProduct.name)
            setString(2, newProduct.price.toPlainString())
            setString(3, newProduct.imageUrl)
        }
    }
}
