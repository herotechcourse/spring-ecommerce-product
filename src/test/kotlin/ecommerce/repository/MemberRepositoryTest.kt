package ecommerce.repository

import ecommerce.dto.RegistrationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
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
        db.sql("DROP TABLE MEMBERS IF EXISTS").update()
        val sql =
            "CREATE TABLE members (id INT AUTO_INCREMENT PRIMARY KEY, email UNIQUE VARCHAR(255), password VARCHAR(255))"
        db.sql(sql).update()
    }

    @Test
    fun save() {
        val user = RegistrationRequest("test@example.com", "test123")
        val saved = memberRepository.save(user)
        val count =
            db.sql("SELECT COUNT(*) FROM members")
                .query(Int::class.java)
                .single()

        assertTrue(saved)
        assertThat(count).isEqualTo(1)
    }
}
