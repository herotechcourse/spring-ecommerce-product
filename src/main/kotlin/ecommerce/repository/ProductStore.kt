package ecommerce.repository

import ecommerce.product.Product
import ecommerce.product.ProductRequest
import ecommerce.product.toEntity
import ecommerce.sql.ConstantsSQL
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

@Repository
class ProductStore(private val jdbcTemplate: JdbcTemplate) {
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
        val sql = ConstantsSQL.SELECT_ALL
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun create(request: ProductRequest): Product {
        val id = createAndReturnId(request)
        return request.toEntity(id)
    }

    fun update(
        id: Long,
        request: ProductRequest,
    ): Product? {
        if (!existsById(id)) return null
        val sql = ConstantsSQL.UPDATE_BY_ID.trimIndent()
        val entity = request.toEntity(id)
        val result = jdbcTemplate.update(sql, entity.name, entity.price, entity.imageUrl, entity.id) == 1
        return if (result) entity else null
    }

    fun deleteById(id: Long): Boolean {
        if (!existsById(id)) return false
        val sql = ConstantsSQL.DELETE_BY_ID.trimIndent()

        return jdbcTemplate.update(sql, id) == 1
    }

    fun existsById(id: Long): Boolean {
        val sql = ConstantsSQL.COUNT_BY_ID
        val found = jdbcTemplate.queryForObject(sql, Int::class.java, id)
        return found != null && found > 0
    }

    private fun createAndReturnId(request: ProductRequest): Long {
        val sql = ConstantsSQL.INSERT.trimIndent()
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            createPreparedStatement(connection, sql, request)
        }, keyHolder)

        return keyHolder.key?.toLong()
            ?: throw IllegalStateException("Failed to retrieve generated ID")
    }

    private fun createPreparedStatement(
        connection: Connection,
        sql: String,
        request: ProductRequest,
    ): PreparedStatement {
        return connection.prepareStatement(sql, arrayOf("id")).apply {
            setString(1, request.name)
            setString(2, request.price)
            setString(3, request.imageUrl)
        }
    }
}
