package ecommerce.repository

import ecommerce.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.simple.JdbcClient

@JdbcTest
class MemberRepositoryTest {
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var db: JdbcClient

    @BeforeEach
    fun setUp() {
        memberRepository = MemberRepository(db)
        db.sql("DROP TABLE IF EXISTS cart_history").update()
        db.sql("DROP TABLE IF EXISTS cart_items").update()
        db.sql("DROP TABLE IF EXISTS carts").update()
        db.sql("DROP TABLE IF EXISTS members").update()

        val sql =
            """
            CREATE TABLE members (
                id INT AUTO_INCREMENT PRIMARY KEY, 
                email VARCHAR(255) UNIQUE,
                password VARCHAR(255), 
                role VARCHAR(100) NOT NULL DEFAULT 'USER'
            )
            """.trimIndent()

        db.sql(sql).update()
    }

    @Test
    fun save() {
        val user = Member(null, "test@example.com", "test123", "USER")
        val id = memberRepository.save(user)
        val count =
            db.sql("SELECT COUNT(*) FROM members")
                .query(Int::class.java)
                .single()

        assertThat(id).isEqualTo(1)
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun findByEmail() {
        val user = Member(null, "test@example.com", "test123", "USER")
        memberRepository.save(user)
        db.sql("SELECT COUNT(*) FROM members")
            .query(Int::class.java)
            .single()

        val member = memberRepository.findByEmail("test@example.com")
        assertThat(member?.email).isEqualTo("test@example.com")
    }
}
