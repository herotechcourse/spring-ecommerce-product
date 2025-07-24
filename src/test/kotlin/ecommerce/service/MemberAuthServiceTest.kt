package ecommerce.service

import ecommerce.dto.auth.LoginRequest
import ecommerce.dto.user.MemberUserDTO
import ecommerce.dto.user.UserRequestDTO
import ecommerce.exception.EntityNotFoundException
import ecommerce.exception.UserAlreadyExistsException
import ecommerce.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberAuthServiceTest {
    @Autowired
    private lateinit var memberAuthService: MemberAuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `throws error if already exists signUp`() {
        val userDTO =
            UserRequestDTO(
                name = "test",
                password = "test123",
                email = "signUpError@test.com",
            )
        val member =
            MemberUserDTO(
                email = userDTO.email,
                password = userDTO.password,
                name = userDTO.name,
            )
        userRepository.create(member)
        assertThrows<UserAlreadyExistsException> { memberAuthService.signUp(userDTO) }
    }

    @Test
    fun signUp() {
        val userDTO =
            UserRequestDTO(
                name = "test",
                password = "test123",
                email = "signUp@test.com",
            )
        val result = memberAuthService.signUp(userDTO)
        assertThat(result.token).isNotEmpty
        assertThat(result.uri).isNotNull
    }

    @Test
    fun `No user found at signIn with email`() {
        val loginRequest =
            LoginRequest(
                password = "test123",
                email = "signInError@test.com",
            )
        assertThrows<EntityNotFoundException> { memberAuthService.logIn(loginRequest) }
    }

    @Test
    fun `No user found at signIn with wrong password`() {
        val userDTO =
            MemberUserDTO(
                name = "test",
                password = "test123",
                email = "signInErrorPassword@test.com",
            )
        userRepository.create(userDTO)
        val loginRequest =
            LoginRequest(
                email = userDTO.email,
                password = "test123456",
            )
        assertThrows<EntityNotFoundException> { memberAuthService.logIn(loginRequest) }
    }

    @Test
    fun signIn() {
        val userDTO =
            MemberUserDTO(
                name = "test",
                password = "test123",
                email = "signInError@test.com",
            )
        userRepository.create(userDTO)
        val loginRequest =
            LoginRequest(
                userDTO.email,
                userDTO.password,
            )
        assertThat(memberAuthService.logIn(loginRequest)).isNotEmpty
    }
}
