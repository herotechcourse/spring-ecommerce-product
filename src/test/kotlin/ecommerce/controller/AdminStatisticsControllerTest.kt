package ecommerce.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ecommerce.entity.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminStatisticsControllerTest(
    @Autowired val mockMvc: MockMvc,
) {
    val objectMapper = jacksonObjectMapper()
    lateinit var adminToken: String
    lateinit var userToken: String
    lateinit var productIds: List<Long>

    @Test
    fun `admin sees top 3 products after user adds 3 items to cart`() {
        register("admin@mail.com", "123456", Role.ADMIN)
        register("user@mail.com", "654321", Role.USER)

        adminToken = login("admin@mail.com", "123456")
        userToken = login("user@mail.com", "654321")

        productIds =
            listOf(
                createProduct("Product A", 9.99),
                createProduct("Product B", 19.99),
                createProduct("Product C", 29.99),
            )

        repeat(3) { addToCart(productIds[0]) }
        repeat(2) { addToCart(productIds[1]) }
        repeat(1) { addToCart(productIds[2]) }

        val result =
            mockMvc.get("/admin/stats/top-products") {
                header("Authorization", "Bearer $adminToken")
            }.andExpect {
                status { isOk() }
            }.andReturn()

        val responseJson = objectMapper.readTree(result.response.contentAsString)
        val names = responseJson.map { it["productName"].asText() }

        assertThat(names).containsExactly("Product A", "Product B", "Product C")
    }

    @Test
    fun `admin sees top 5 products in correct order after user adds 7 products to cart (some of which multiple times)`() {
        register("admin@mail.com", "123456", Role.ADMIN)
        register("user@mail.com", "654321", Role.USER)

        adminToken = login("admin@mail.com", "123456")
        userToken = login("user@mail.com", "654321")

        productIds =
            listOf(
                createProduct("Product A", 10.0),
                createProduct("Product B", 20.0),
                createProduct("Product C", 30.0),
                createProduct("Product D", 40.0),
                createProduct("Product E", 50.0),
                createProduct("Product F", 60.0),
                createProduct("Product G", 70.0),
            )

        addToCart(productIds[0])
        addToCart(productIds[1])
        repeat(3) { addToCart(productIds[2]) }
        addToCart(productIds[3])
        addToCart(productIds[4])
        repeat(2) { addToCart(productIds[5]) }
        addToCart(productIds[6])

        val result =
            mockMvc.get("/admin/stats/top-products") {
                header("Authorization", "Bearer $adminToken")
            }.andExpect {
                status { isOk() }
            }.andReturn()

        val responseJson = objectMapper.readTree(result.response.contentAsString)
        val names = responseJson.map { it["productName"].asText() }

        // Expect: C (3), F (2), G (1), E (1), D (1) - latest 1x entries take order by recency
        assertThat(names).containsExactly("Product C", "Product F", "Product G", "Product E", "Product D")
    }

    @Test
    fun `admin sees recently active users who added to cart in last 7 days`() {
        register("admin@mail.com", "123456", Role.ADMIN)
        register("user1@mail.com", "654321", Role.USER)
        register("user2@mail.com", "654321", Role.USER)
        register("user3@mail.com", "654321", Role.USER) // inactive user

        adminToken = login("admin@mail.com", "123456")
        val user1Token = login("user1@mail.com", "654321")
        val user2Token = login("user2@mail.com", "654321")
        login("user3@mail.com", "654321") // inactive user

        val productId = createProduct("Dummy Product", 9.99)

        addToCart(user1Token, productId)
        addToCart(user1Token, productId) // user 1 second add
        addToCart(user2Token, productId)

        val result =
            mockMvc.get("/admin/stats/recent-users") {
                header("Authorization", "Bearer $adminToken")
            }.andExpect {
                status { isOk() }
            }.andReturn()

        val json = objectMapper.readTree(result.response.contentAsString)
        val emails = json.map { it["email"].asText() }

        assertThat(emails).containsExactlyInAnyOrder("user1@mail.com", "user2@mail.com")

        val user1Count = emails.count { it == "user1@mail.com" }
        assertThat(user1Count).isEqualTo(1) // user 1 only once listed although 2 adds

        assertThat(json[0]["userId"].asLong()).isNotNull()
        assertThat(emails).doesNotContain("user3@mail.com") // ensure inactive user is excluded
    }

    // helper functions

    private fun register(
        email: String,
        password: String,
        role: Role,
    ) {
        mockMvc.post("/api/users/register") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
            {
                "email": "$email",
                "password": "$password",
                "role": "${role.name}"
            }
            """.trimIndent()
        }.andExpect { status { isOk() } }
    }

    private fun login(
        email: String,
        password: String,
    ): String {
        val result =
            mockMvc.post("/api/users/login") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    """
                    {
                        "email": "$email",
                        "password": "$password"
                    }
                    """.trimIndent()
            }.andExpect { status { isOk() } }.andReturn()

        return objectMapper.readTree(result.response.contentAsString)["token"].asText()
    }

    private fun createProduct(
        name: String,
        price: Double,
    ): Long {
        val result =
            mockMvc.post("/api/products") {
                header("Authorization", "Bearer $adminToken")
                contentType = MediaType.APPLICATION_JSON
                content =
                    """
                    {
                        "name": "$name",
                        "price": $price,
                        "imageUrl": "https://tastesbetterfromscratch.com/wp-content/uploads/2020/08/Pistachio-Ice-Cream-5-300x300.jpg"
                    }
                    """.trimIndent()
            }.andExpect { status { isCreated() } }.andReturn()

        return result.response.getHeader("Location")!!.substringAfterLast("/").toLong()
    }

    private fun addToCart(productId: Long) {
        mockMvc.post("/api/cart") {
            header("Authorization", "Bearer $userToken")
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "productId": $productId,
                    "quantity": 1
                }
                """.trimIndent()
        }.andExpect { status { isCreated() } }
    }

    private fun addToCart(
        token: String,
        productId: Long,
    ) {
        mockMvc.post("/api/cart") {
            header("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "productId": $productId,
                    "quantity": 1
                }
                """.trimIndent()
        }.andExpect { status { isCreated() } }
    }
}
