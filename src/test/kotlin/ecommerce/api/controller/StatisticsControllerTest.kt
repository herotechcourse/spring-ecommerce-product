package ecommerce.api.controller

import ecommerce.dto.MemberDTO
import ecommerce.dto.ProductStatsDTO
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatisticsControllerTest {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `GET top-products returns list of product stats`() {
        val response = restTemplate.getForEntity("/api/stats/top-products", Array<ProductStatsDTO>::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        Assertions.assertThat(body).isNotNull
        if (body != null) {
            Assertions.assertThat(body.size).isLessThanOrEqualTo(5)
        }
    }

    @Test
    fun `GET recent-active-members returns list of member DTOs`() {
        val response = restTemplate.getForEntity("/api/stats/recent-active-members", Array<MemberDTO>::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        Assertions.assertThat(body).isNotNull
        if (body != null) {
            val emails = body.map { it.email }
            Assertions.assertThat(emails).doesNotHaveDuplicates()
            Assertions.assertThat(emails).noneMatch { it.isBlank() }
        }
    }
}
