package ecommerce.service

import ecommerce.dto.ActiveUsersResponse
import ecommerce.dto.TopProductStats
import ecommerce.repository.AdminStatsRepository
import org.springframework.stereotype.Service

@Service
class AdminStatsService(
    private val adminStatsRepository: AdminStatsRepository,
) {
    fun getTopProducts(): List<TopProductStats> {
        return adminStatsRepository.get5MostAddedProducts()
    }

    fun getTopActiveUsers(): List<ActiveUsersResponse> {
        return adminStatsRepository.getTop5ActiveUsers()
    }
}
