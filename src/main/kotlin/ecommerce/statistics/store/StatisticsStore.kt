package ecommerce.statistics.store

import ecommerce.auth.model.MemberDTO
import ecommerce.statistics.model.ProductStatsDTO

interface StatisticsStore {
    fun find5MostAddedProductsLast30Days(): List<ProductStatsDTO>

    fun findMembersAddedToCartLast7Days(): List<MemberDTO>
}
