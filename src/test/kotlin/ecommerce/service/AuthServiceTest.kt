package ecommerce.service

import ecommerce.auth.JwtTokenProvider
import ecommerce.dto.LoginForm
import ecommerce.dto.RegisterForm
import ecommerce.exception.AuthorizationException
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/member.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class AuthServiceTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val authService: AuthService,
) {
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
