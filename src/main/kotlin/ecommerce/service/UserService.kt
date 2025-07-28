package ecommerce.service

import ecommerce.entity.User
import ecommerce.repository.UserRepository
import org.springframework.stereotype.Service

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

    fun checkPassword(
        user: User,
        rawPassword: String,
    ): Boolean {
        return rawPassword == user.password
    }
}
