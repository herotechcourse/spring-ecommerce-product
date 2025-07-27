package ecommerce.api.statistics

import ecommerce.auth.model.MemberDTO
import ecommerce.statistics.model.ProductStatsDTO
import ecommerce.statistics.service.StatisticsService
import ecommerce.statistics.store.JdbcStatisticsStore
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

class StatisticsServiceTest {
    @Mock
    lateinit var statisticsStore: JdbcStatisticsStore

    @InjectMocks
    lateinit var statisticsService: StatisticsService

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getTop5MostAddedProductsLast30Days returns list from repository`() {
        val expected = listOf(ProductStatsDTO("Product 1", 12, LocalDateTime.now()))

        Mockito.`when`(statisticsStore.find5MostAddedProductsLast30Days()).thenReturn(expected)

        val actual = statisticsService.getTop5MostAddedProductsLast30Days()
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getMembersAddedToCartLast7Days returns list from repository`() {
        val expected = listOf(MemberDTO(1L, "a@gmail.com"))

        Mockito.`when`(statisticsStore.findMembersAddedToCartLast7Days()).thenReturn(expected)

        val actual = statisticsService.getMembersAddedToCartLast7Days()
        Assertions.assertThat(actual).isEqualTo(expected)
    }
}
