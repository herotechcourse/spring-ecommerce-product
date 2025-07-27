package ecommerce.api.statistics

import ecommerce.auth.model.MemberDTO
import ecommerce.statistics.model.ProductStatsDTO
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        assertThat(body).isNotNull
        if (body != null) {
            assertThat(body.size).isLessThanOrEqualTo(5)
        }
    }

    @Test
    fun `GET recent-active-members returns list of member DTOs`() {
        val response = restTemplate.getForEntity("/api/stats/recent-active-members", Array<MemberDTO>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        assertThat(body).isNotNull
        if (body != null) {
            val emails = body.map { it.email }
            assertThat(emails).doesNotHaveDuplicates()
            assertThat(emails).noneMatch { it.isBlank() }
        }
    }
}
