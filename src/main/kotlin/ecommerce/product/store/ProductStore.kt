package ecommerce.product.store

import ecommerce.product.data.Product
import ecommerce.product.data.ProductMapper
import ecommerce.product.data.ProductRequest
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

    fun findById(id: Long): Product? {
        val sql = ConstantsSQL.SELECT_BY_ID
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
        request: ProductRequest,
    ): Product {
        require(existsById(id)) { "Product with id $id not found" }

        val sql = ConstantsSQL.UPDATE_BY_ID.trimIndent()
        val entity = ProductMapper.toEntity(request, id)!!
        jdbcTemplate.update(sql, entity.name, entity.price, entity.imageUrl, entity.id)

        return entity
    }

    fun deleteById(id: Long) {
        require(existsById(id)) { "Product with id $id not found" }

        val sql = ConstantsSQL.DELETE_BY_ID.trimIndent()
        jdbcTemplate.update(sql, id)
    }

    fun existsById(id: Long): Boolean {
        val sql = ConstantsSQL.COUNT_BY_ID
        val found = jdbcTemplate.queryForObject(sql, Int::class.java, id)
        return found != null && found > 0
    }

    fun existsByName(name: String): Boolean {
        val sql = ConstantsSQL.COUNT_BY_NAME
        val found = jdbcTemplate.queryForObject(sql, Int::class.java, name)
        return found != null && found > 0
    }

    fun insertWithKeyholder(request: ProductRequest): Long {
        require(!existsByName(request.name)) { "Product with name ${request.name} already exists" }
        val sql = ConstantsSQL.INSERT.trimIndent()
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            createPreparedStatement(connection, sql, request)
        }, keyHolder)

        val id = keyHolder.key?.toLong()
        require(id != null) {
            "Failed to retrieve generated ID after inserting product '${request.name}'. " +
                "Database key generation failed."
        }
        return id
    }

    private fun createPreparedStatement(
        connection: Connection,
        sql: String,
        request: ProductRequest,
    ): PreparedStatement {
        return connection.prepareStatement(sql, arrayOf("id")).apply {
            setString(1, request.name)
            setString(2, request.price.toPlainString())
            setString(3, request.imageUrl)
        }
    }
}
