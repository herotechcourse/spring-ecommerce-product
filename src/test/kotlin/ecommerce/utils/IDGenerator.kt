package ecommerce.utils

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.UUID

class IDGenerator {
    @Test
    fun generatePasswordHashes() {
        val passwordEncoder = BCryptPasswordEncoder()
        println("Admin Password Hash: ${passwordEncoder.encode("Admin_passw0rd")}")
        println("User1 Password Hash: ${passwordEncoder.encode("User1_password")}")
        println("User2 Password Hash: ${passwordEncoder.encode("User2_password")}")
    }

    @Test
    fun generateUUIDs() {
        println("UUID 1: ${UUID.randomUUID()}")
        println("UUID 2: ${UUID.randomUUID()}")
        println("UUID 3: ${UUID.randomUUID()}")
    }
}
