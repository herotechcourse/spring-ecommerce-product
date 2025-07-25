package ecommerce.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MemberControllerTest {
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
                MockMvcRequestBuilders.post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val response = result.response
        val responseBody = response.contentAsString
        val token = objectMapper.readTree(responseBody).get("token").asText()

        Assertions.assertThat(response.status).isEqualTo(200)
        Assertions.assertThat(token).startsWith("ey")
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
                MockMvcRequestBuilders.post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()

        val response = result.response

        Assertions.assertThat(response.status).isEqualTo(400)
        Assertions.assertThat(response.contentAsString).contains("email", "must not be blank")
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
                MockMvcRequestBuilders.post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()

        val response = result.response

        Assertions.assertThat(response.status).isEqualTo(400)
        Assertions.assertThat(response.contentAsString).contains("password", "size must be")
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
            MockMvcRequestBuilders.post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()

        val response = result.response

        Assertions.assertThat(response.status).isEqualTo(400)
        Assertions.assertThat(response.contentAsString).contains("Email already in use")
    }

    @Test
    fun `logging in with correct credentials should return 200 and JWT token`() {
        val registerRequest =
            """
            {
              "email": "loginuser@example.com",
              "password": "loginpass123"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest),
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val loginRequest =
            """
            {
              "email": "loginuser@example.com",
              "password": "loginpass123"
            }
            """.trimIndent()

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequest),
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val responseBody = result.response.contentAsString
        val token = objectMapper.readTree(responseBody).get("token").asText()

        Assertions.assertThat(token).startsWith("ey")
    }

    @Test
    fun `logging in with wrong password should return 403`() {
        val registerRequest =
            """
            {
              "email": "wrongpass@example.com",
              "password": "correctpass"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest),
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val loginRequest =
            """
            {
              "email": "wrongpass@example.com",
              "password": "wrongpass"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest),
        ).andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `logging in with non-existent email should return 403`() {
        val loginRequest =
            """
            {
              "email": "doesnotexist@example.com",
              "password": "testPassword"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest),
        ).andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `logging in with blank email should return 400`() {
        val loginRequest =
            """
            {
              "email": "",
              "password": "validpass"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest),
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
