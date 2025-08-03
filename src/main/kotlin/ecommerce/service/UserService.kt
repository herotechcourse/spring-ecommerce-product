package ecommerce.service

import ecommerce.dto.LoggedInUser
import ecommerce.entity.Role
import ecommerce.entity.User
import ecommerce.exception.EmailAlreadyUsedException
import ecommerce.exception.InvalidRoleException
import ecommerce.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun register(
        email: String,
        password: String,
        roleStr: String? = null,
    ): Long {
        if (userRepository.existsByEmail(email)) {
            throw EmailAlreadyUsedException("Email already in use.")
        }

        val role = validateRole(roleStr)

        val user = User(email = email, password = password, role = role)
        return userRepository.create(user)
    }

    private fun validateRole(roleStr: String?): Role {
        return roleStr?.let {
            try {
                Role.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidRoleException("Invalid role: $it")
            }
        } ?: Role.USER
    }

    fun getByEmail(email: String): User? {
        return userRepository.getByEmail(email)
    }

    fun login(
        email: String,
        providedPassword: String,
    ): LoggedInUser {
        val user = getByEmail(email) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.")
        if (!user.checkPassword(providedPassword)) throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.")
        return user.toLoggedInUser()
    }
}
