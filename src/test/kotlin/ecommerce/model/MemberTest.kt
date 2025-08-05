package ecommerce.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import kotlin.test.Test

class MemberTest {
    @Test
    fun `should throw illegal exception when email is blank`() {
        assertThatThrownBy {
            Member(
                id = 1L,
                email = "",
                password = "1234",
                role = Role.USER,
                name = "Jon",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Email cannot be blank")
    }

    @Test
    fun `should throw illegal exception when email is missing basic components`() {
        assertThatThrownBy {
            Member(
                id = 1L,
                email = "test.test",
                password = "1234",
                role = Role.USER,
                name = "Jon",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Email should contain a @ and a .com")
    }

    @Test
    fun `should throw illegal exception when password is empty`() {
        assertThatThrownBy {
            Member(
                id = 1L,
                email = "test@test.com",
                password = "",
                role = Role.USER,
                name = "Jon",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Password cannot be blank")
    }

    @Test
    fun `should throw illegal exception when password is is not between 4 and 8 characters`() {
        assertThatThrownBy {
            Member(
                id = 1L,
                email = "test@test.com",
                password = "12",
                role = Role.USER,
                name = "Jon",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Password with a minimum of 4  and maximum of 8 characters long")

        assertThatThrownBy {
            Member(
                id = 1L,
                email = "test@test.com",
                password = "123456789",
                role = Role.USER,
                name = "Jon",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Password with a minimum of 4  and maximum of 8 characters long")
    }
}
