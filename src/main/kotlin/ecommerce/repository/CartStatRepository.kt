package ecommerce.repository

import ecommerce.dto.ActiveMemberStat
import ecommerce.dto.TopProductStat

interface CartStatRepository {
    fun findTop5MostAddedProductsInLast30Days(): List<TopProductStat>

    fun findActiveMembersInLast7Days(): List<ActiveMemberStat>
}
