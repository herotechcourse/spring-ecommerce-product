package ecommerce.service

import ecommerce.dto.RecentActiveMemberResponse
import ecommerce.dto.TopProductStat
import ecommerce.repository.CartItemHistoryRepository
import org.springframework.stereotype.Service

@Service
class StatisticsService(private val cartItemHistoryRepository: CartItemHistoryRepository) {
    fun getTop5ProductsLast30Days(): List<TopProductStat> {
        return cartItemHistoryRepository.findTop5MostAddedProducts().map {
            TopProductStat(
                productName = it["product_name"].toString(),
                addCount = (it["add_count"] as Number).toInt(),
                latestAddedAt = it["latest_added_at"].toString(),
            )
        }
    }

    fun getRecentlyActiveMembers(): List<RecentActiveMemberResponse> {
        return cartItemHistoryRepository.findRecentlyActiveMembers()
    }
}
