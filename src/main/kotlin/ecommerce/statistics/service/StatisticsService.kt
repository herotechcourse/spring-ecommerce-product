package ecommerce.statistics.service

import ecommerce.auth.model.MemberDTO
import ecommerce.statistics.model.ProductStatsDTO
import ecommerce.statistics.store.StatisticsStore
import org.springframework.stereotype.Service

@Service
class StatisticsService(private val statisticsStore: StatisticsStore) {
    fun getTop5MostAddedProductsLast30Days(): List<ProductStatsDTO> {
        return statisticsStore.find5MostAddedProductsLast30Days()
    }

    fun getMembersAddedToCartLast7Days(): List<MemberDTO> {
        return statisticsStore.findMembersAddedToCartLast7Days()
    }
}
