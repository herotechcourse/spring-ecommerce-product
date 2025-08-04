package ecommerce.statistics.service

import ecommerce.auth.model.MemberDTO
import ecommerce.statistics.model.ProductStatsDTO
import ecommerce.statistics.store.JdbcStatisticsStore
import org.springframework.stereotype.Service

@Service
class StatisticsService(private val jdbcStatisticsStore: JdbcStatisticsStore) {
    fun getTop5MostAddedProductsLast30Days(): List<ProductStatsDTO> {
        return jdbcStatisticsStore.find5MostAddedProductsLast30Days()
    }

    fun getMembersAddedToCartLast7Days(): List<MemberDTO> {
        return jdbcStatisticsStore.findMembersAddedToCartLast7Days()
    }
}
