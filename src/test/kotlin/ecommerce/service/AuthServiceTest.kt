package ecommerce.service

import ecommerce.auth.JwtTokenProvider
import ecommerce.dao.JdbcMemberDAO
import ecommerce.dto.LoginForm
import ecommerce.dto.RegisterForm
import ecommerce.exception.AuthorizationException
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.math.E

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthServiceTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired private lateinit var jdbcMemberDAO: JdbcMemberDAO

    @Autowired private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        jdbcMemberDAO = JdbcMemberDAO(jdbcTemplate)
        jwtTokenProvider = JwtTokenProvider()
        authService = AuthService(jdbcMemberDAO, jwtTokenProvider)

        jdbcTemplate.execute("DROP TABLE member CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE member
            (
                id       LONG         NOT NULL AUTO_INCREMENT,
                email    VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            );""",
        )

        val query =
            """INSERT INTO member (email, password) VALUES ( 'san@htc.com', 'san1234');
            INSERT INTO member (email, password) VALUES ( 'dan@htc.com', 'dan1234');"""
        jdbcTemplate.batchUpdate(query)
    }

    @Test
    fun `registerMember() - should throw an exception when email already exists`() {
        val email = "dan@htc.com"
        val password = "test1234"
        val registerForm = RegisterForm(email, password)
        assertThrows<MemberEmailAlreadyExistsException> { authService.registerMember(registerForm) }
    }

    @Test
    fun `registerMember() - should return a member with id when registration information is valid`() {
        val email = "test@email.com"
        val password = "test1234"
        val registerForm = RegisterForm(email, password)
        val registeredMember = authService.registerMember(registerForm)
        assertThat(registeredMember.id).isNotNull()
        assertThat(registeredMember.email).isEqualTo(email)
        assertThat(registeredMember.password).isEqualTo(password)
    }

    @Test
    fun `loginMember() - should throw an exception when email does not exists`() {
        val email = "test@email.com"
        val password = "test1234"
        val loginForm = LoginForm(email, password)
        assertThrows<AuthorizationException> { authService.loginMember(loginForm) }
    }

    @Test
    fun `loginMember() - should throw an exception when password is not valid`() {
        val email = "dan@htc.com"
        val password = "test1234"
        val loginForm = LoginForm(email, password)
        assertThrows<AuthorizationException> { authService.loginMember(loginForm) }
    }

    @Test
    fun `loginMember() - should return an AuthResponse with access token when login credentials are valid`() {
        val email = "dan@htc.com"
        val password = "dan1234"
        val loginForm = LoginForm(email, password)
        val authResponse = authService.loginMember(loginForm)
        assertThat(authResponse.accessToken).isNotNull()
    }

    @Test
    fun `findMemberByToken() - should return a member with id when token is valid`() {
        val token = jwtTokenProvider.createToken(EMAIL)
        val member = authService.findMemberByToken(token)
        assertThat(member).isInstanceOf(Member::class.java)
        assertThat(member.id).isNotNull()
    }

    companion object {
        private const val EMAIL = "san@htc.com"
    }
}
