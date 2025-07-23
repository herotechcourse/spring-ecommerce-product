package ecommerce.repository

import ecommerce.dto.UserDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserRepositoryTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Test fun existsByEmail() {
        val user =
            UserDTO(
                name = "test",
                password = "test123",
                email = "existsByEmail@test.com",
            )
        userRepository.create(user)
        val existsByEmail = userRepository.existsByEmail("existsByEmail@test.com")
        assertThat(existsByEmail).isTrue
    }

    @Test fun `returns false if not existsByEmail`() {
        val existsByEmail = userRepository.existsByEmail("none@test.com")
        assertThat(existsByEmail).isFalse
    }

    @Test
    fun create() {
        val user =
            UserDTO(
                name = "test",
                password = "test123",
                email = "create@test.com",
            )
        userRepository.create(user)
        val existsByEmail = userRepository.existsByEmail("create@test.com")
        assertThat(existsByEmail).isTrue
    }

    @Test
    fun findByEmailAndPassword() {
        val userDTO =
            UserDTO(
                name = "test",
                password = "test123",
                email = "findByEmailAndPassword@test.com",
            )
        userRepository.create(userDTO)

        val user = userRepository.findByEmailAndPassword(userDTO.email, userDTO.password)
        assertThat(user).isNotNull
    }

    @Test
    fun `returns null for findByEmailAndPassword`() {
        val userDTO =
            UserDTO(
                name = "test",
                password = "test123",
                email = "findByEmailAndPasswordFalse@test.com",
            )
        userRepository.create(userDTO)

        val user = userRepository.findByEmailAndPassword(userDTO.email, "")
        assertThat(user).isNull()
    }
}
