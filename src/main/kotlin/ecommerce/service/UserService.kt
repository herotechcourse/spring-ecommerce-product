package ecommerce.service

import ecommerce.entity.User
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
        role: String?,
    ): Long {
        if (userRepository.existsByEmail(email)) throw IllegalArgumentException("Email already in use.")

        val user = User(email = email, password = password, role = role ?: "USER")
        return userRepository.create(user)
    }

    fun getByEmail(email: String): User? {
        return userRepository.getByEmail(email)
    }

    fun login(
        email: String,
        providedPassword: String,
    ): User {
        val user = getByEmail(email) ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials.")
        if (!user.checkPassword(providedPassword)) throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials.")
        return user
    }
}
