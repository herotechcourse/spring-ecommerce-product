package ecommerce

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminAuthInterceptorTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should not allow access to user for products stats`() {
        val response = mockMvc.perform(get("/admin/statistics/top-products"))
            .andExpect(status().isUnauthorized)
            .andReturn()

        val products = response.response.contentAsString // Deserialize and assert here
    }

    @Test
    fun `should not allow access to user for members stats`() {
        val response = mockMvc.perform(get("/admin/statistics/active-members"))
            .andExpect(status().isUnauthorized)
            .andReturn()

        val members = response.response.contentAsString // Deserialize and assert here
    }

}
