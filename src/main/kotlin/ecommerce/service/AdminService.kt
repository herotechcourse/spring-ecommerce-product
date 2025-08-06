package ecommerce.service

import ecommerce.dto.MemberStatsResponse
import ecommerce.dto.ProductStatsResponse
import ecommerce.dto.StatsQueryParams
import ecommerce.repository.StatsRepository
import ecommerce.sql.SortOption
import org.springframework.stereotype.Service

@Service
class AdminService(private val repository: StatsRepository) {
    fun getTopProducts(params: StatsQueryParams): List<ProductStatsResponse> {
        val sort = SortOption.from(params.sort) ?: return emptyList()
        return repository.findTopProducts(params.limit, params.days, sort)
    }

    fun getActiveMembers(params: StatsQueryParams): List<MemberStatsResponse> {
        val sort = SortOption.from(params.sort) ?: return emptyList()
        return repository.findAllActiveMembers(params.days, sort)
    }
}
