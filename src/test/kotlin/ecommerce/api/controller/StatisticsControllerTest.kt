package ecommerce.api.controller

import ecommerce.dto.MemberDTO
import ecommerce.dto.ProductStatsDTO
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatisticsControllerTest {
    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `GET top-products returns list of product stats`() {
        val products =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/api/stats/top-products")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", ProductStatsDTO::class.java)

        assertThat(products.size).isLessThanOrEqualTo(5)
        assertThat(products).allMatch { it.timesAdded >= 0 }
    }

    @Test
    fun `GET recent-active-members returns list of member DTOs`() {
        val members =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/api/stats/recent-active-members")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", MemberDTO::class.java)

        val emails = members.map { it.email }
        assertThat(emails).doesNotHaveDuplicates()
        assertThat(emails).noneMatch { it.isBlank() }
    }
}
