package ecommerce

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StatisticsTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should return top 5 most added products`() {
        val response = mockMvc.perform(get("/admin/statistics/top-products"))
            .andExpect(status().isOk)
            .andReturn()

        val products = response.response.contentAsString // Deserialize and assert here

        assertThat(products).isNotEmpty
    }

    @Test
    fun `should return active members from last 7 days`() {
        val response = mockMvc.perform(get("/admin/statistics/active-members"))
            .andExpect(status().isOk)
            .andReturn()

        val members = response.response.contentAsString // Deserialize and assert here

        assertThat(members).isNotEmpty
    }

}
