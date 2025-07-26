package ecommerce.service

import ecommerce.dto.MemberStatsDto
import ecommerce.dto.ProductStatsDto
import ecommerce.repository.StatRepository
import org.springframework.stereotype.Service

@Service
class StatService(private val statRepository: StatRepository) {
    fun getActiveMembersInThePast7Days(): List<MemberStatsDto> {
        return statRepository.getActiveMembersInThePast7Days()
    }

    fun getTop5ProductsInThePast30Days(): List<ProductStatsDto> {
        return statRepository.getTop5ProductsInThePast30Days()
    }
}
