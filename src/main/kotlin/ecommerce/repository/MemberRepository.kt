package ecommerce.repository

import ecommerce.dto.RegistrationRequest
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(private val db: JdbcClient) {
    fun save(member: RegistrationRequest): Boolean {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        val sql = "INSERT INTO members (email, password) VALUES (?,?)"
        return db
            .sql(sql)
            .params(member.email, member.password)
            .update(keyHolder) > 0
    }

    fun existsByEmail(email: String): Boolean {
        return db
            .sql("SELECT COUNT(*) FROM members WHERE email =?")
            .param(email)
            .query(Int::class.java)
            .single() > 0
    }
}
