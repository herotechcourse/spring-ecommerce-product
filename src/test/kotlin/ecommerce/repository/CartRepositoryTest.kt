package ecommerce.repository

import ecommerce.dto.user.MemberUserDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartRepositoryTest {
    @Autowired
    private lateinit var cartRepository: CartRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun findMembersCart() {
        val userRequestDTO =
            MemberUserDTO(
                email = "findMembersCart@test.com",
                password = "test123",
                name = "test",
            )
        val userID = userRepository.create(userRequestDTO)
        cartRepository.createCartForUser(userID)

        val cartID = cartRepository.findMembersCart(userID)
        assertThat(cartID).isNotNull
    }

    @Test
    fun `return null for findMembersCart`() {
        val cartID = cartRepository.findMembersCart(-1)
        assertThat(cartID).isNull()
    }
}
