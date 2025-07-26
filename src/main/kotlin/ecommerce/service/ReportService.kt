package ecommerce.service

import ecommerce.dto.report.MemberCartActivityDTO
import ecommerce.dto.report.ProductCartCountDTO
import ecommerce.repository.CartEventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class ReportService(private val cartEventRepository: CartEventRepository) {
    fun findTop5MostAddedProductsInLast30Days(): List<ProductCartCountDTO> {
        val startDate = LocalDateTime.now().minus(30, ChronoUnit.DAYS)
        return cartEventRepository.findTop5MostAddedProductsInLast30Days(startDate)
    }

    fun getMembersWhoAddedItemsInLast7Days(): List<MemberCartActivityDTO> {
        val startDate = LocalDateTime.now().minus(7, ChronoUnit.DAYS)
        return cartEventRepository.findMembersWhoAddedItemsInLastDays(startDate)
    }
}
