package ecommerce.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtServiceTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `registering a new user should return 200 and JWT token`() {
        val requestBody =
            """
            {
              "email": "testuser@example.com",
              "password": "testpass123"
            }
            """.trimIndent()

        val result =
            mockMvc.perform(
                post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(status().isOk)
                .andReturn()

        val response = result.response
        val responseBody = response.contentAsString
        val token = objectMapper.readTree(responseBody).get("token").asText()

        assertThat(response.status).isEqualTo(200)
        assertThat(token).isNotEmpty
    }

    @Test
    fun `registering with empty email should return 400`() {
        val requestBody =
            """
            {
              "email": "",
              "password": "validpass123"
            }
            """.trimIndent()

        val result =
            mockMvc.perform(
                post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(status().isBadRequest)
                .andReturn()

        val response = result.response

        assertThat(response.status).isEqualTo(400)
        assertThat(response.contentAsString).contains("email", "must not be blank")
    }

    @Test
    fun `registering with too short password should return 400`() {
        val requestBody =
            """
            {
              "email": "user@example.com",
              "password": "123"
            }
            """.trimIndent()

        val result =
            mockMvc.perform(
                post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(status().isBadRequest)
                .andReturn()

        val response = result.response

        assertThat(response.status).isEqualTo(400)
        assertThat(response.contentAsString).contains("password", "size must be")
    }

    @Test
    fun `registering with existing email should return 400`() {
        val requestBody =
            """
            {
              "email": "duplicate@example.com",
              "password": "validpass123"
            }
            """.trimIndent()

        mockMvc.perform(
            post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isOk)

        val result =
            mockMvc.perform(
                post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(status().isBadRequest)
                .andReturn()

        val response = result.response

        assertThat(response.status).isEqualTo(400)
        assertThat(response.contentAsString).contains("Email already in use")
    }
}
