package ecommerce.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
    @Autowired val mockMvc: MockMvc
) {

    val objectMapper = jacksonObjectMapper()
    lateinit var adminToken: String
    lateinit var userToken: String
    lateinit var productIds: List<Long>

    @Test
    fun `admin sees top 3 products after user adds 3 items to cart`() {
        register("admin@mail.com", "123456", "ADMIN")
        register("user@mail.com", "654321", "USER")

        adminToken = login("admin@mail.com", "123456")
        userToken = login("user@mail.com", "654321")

        productIds = listOf(
            createProduct("Product A", 9.99, ),
            createProduct("Product B", 19.99),
            createProduct("Product C", 29.99)
        )

        repeat(3) { addToCart(productIds[0]) }
        repeat(2) { addToCart(productIds[1]) }
        repeat(1) { addToCart(productIds[2]) }

        val result = mockMvc.get("/admin/stats/top-products") {
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
        register("admin@mail.com", "123456", "ADMIN")
        register("user@mail.com", "654321", "USER")

        adminToken = login("admin@mail.com", "123456")
        userToken = login("user@mail.com", "654321")

        productIds = listOf(
            createProduct("Product A", 10.0),
            createProduct("Product B", 20.0),
            createProduct("Product C", 30.0), // add 3 times
            createProduct("Product D", 40.0),
            createProduct("Product E", 50.0),
            createProduct("Product F", 60.0), // add 2 times
            createProduct("Product G", 70.0)
        )

        addToCart(productIds[0])
        addToCart(productIds[1])
        repeat(3) { addToCart(productIds[2]) }
        addToCart(productIds[3])
        addToCart(productIds[4])
        repeat(2) { addToCart(productIds[5]) }
        addToCart(productIds[6])

        val result = mockMvc.get("/admin/stats/top-products") {
            header("Authorization", "Bearer $adminToken")
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val responseJson = objectMapper.readTree(result.response.contentAsString)
        val names = responseJson.map { it["productName"].asText() }

        // Expect: C (3), F (2), G (1), E (1), D (1) - latest 1x entries take order by recency
        assertThat(names).containsExactly("Product C", "Product F", "Product G", "Product E", "Product D")
    }

    // helper functions

    private fun register(email: String, password: String, role: String) {
        mockMvc.post("/api/members/register") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "$email",
                    "password": "$password",
                    "role": "$role"
                }
            """.trimIndent()
        }.andExpect { status { isOk() } }
    }

    private fun login(email: String, password: String): String {
        val result = mockMvc.post("/api/members/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "$email",
                    "password": "$password"
                }
            """.trimIndent()
        }.andExpect { status { isOk() } }.andReturn()

        return objectMapper.readTree(result.response.contentAsString)["token"].asText()
    }

    private fun createProduct(name: String, price: Double): Long {
        val result = mockMvc.post("/api/products") {
            header("Authorization", "Bearer $adminToken")
            contentType = MediaType.APPLICATION_JSON
            content = """
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
            content = """
                {
                    "productId": $productId,
                    "quantity": 1
                }
            """.trimIndent()
        }.andExpect { status { isCreated() } }
    }
}
