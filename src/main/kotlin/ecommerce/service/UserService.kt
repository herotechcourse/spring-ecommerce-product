package ecommerce.service

import ecommerce.entity.User
import ecommerce.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(email: String, rawPassword: String): Long {
        if (userRepository.existsByEmail(email)) throw IllegalArgumentException("Email already in use")

        val hashedPassword = passwordEncoder.encode(rawPassword)
        val user = User(email = email, passwordHash = hashedPassword, role = "USER")
        return userRepository.create(user)
    }
}
