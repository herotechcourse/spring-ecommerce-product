package ecommerce.service

import ecommerce.dto.TokenRequest
import ecommerce.dto.UserDTO
import ecommerce.exception.EntityNotFoundException
import ecommerce.exception.UserAlreadyExistsException
import ecommerce.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthServiceTest {
    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `throws error if already exists signUp`() {
        val userDTO = UserDTO(
            name = "test",
            password = "test123",
            email = "signUpError@test.com"
        )
        userRepository.create(userDTO)
        assertThrows<UserAlreadyExistsException> { authService.signUp(userDTO) }
    }

    @Test
    fun signUp() {
        val userDTO = UserDTO(
            name = "test",
            password = "test123",
            email = "signUp@test.com"
        )
        val result = authService.signUp(userDTO)
        assertThat(result.token).isNotEmpty
        assertThat(result.uri).isNotNull
    }

    @Test
    fun `No user found at signIn with email`() {
        val tokenRequest = TokenRequest(
            password = "test123",
            email = "signInError@test.com"
        )
        assertThrows<EntityNotFoundException> { authService.logIn(tokenRequest) }
    }

    @Test
    fun `No user found at signIn with wrong password`() {
        val userDTO = UserDTO(
            name = "test",
            password = "test123",
            email = "signInErrorPassword@test.com"
        )
        userRepository.create(userDTO)
        val tokenRequest = TokenRequest(
            email = userDTO.email,
            password = "test123456",
        )
        assertThrows<EntityNotFoundException> { authService.logIn(tokenRequest) }
    }

    @Test
    fun signIn() {
        val userDTO = UserDTO(
            name = "test",
            password = "test123",
            email = "signInError@test.com"
        )
        userRepository.create(userDTO)
        val tokenRequest = TokenRequest(
            userDTO.email,
            userDTO.password
        )
        assertThat(authService.logIn(tokenRequest)).isNotEmpty
    }
}
