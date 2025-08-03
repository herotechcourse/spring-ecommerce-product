package ecommerce.service

import ecommerce.dto.RecentActiveUserResponse
import ecommerce.dto.TopProductStat
import ecommerce.repository.CartItemHistoryRepository
import org.springframework.stereotype.Service

@Service
class StatisticsService(private val cartItemHistoryRepository: CartItemHistoryRepository) {
    fun getTop5ProductsLast30Days(): List<TopProductStat> {
        return cartItemHistoryRepository.findTop5MostAddedProducts()
    }

    fun getRecentlyActiveUser(): List<RecentActiveUserResponse> {
        return cartItemHistoryRepository.findRecentlyActiveUsers()
    }
}
