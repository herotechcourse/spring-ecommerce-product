package ecommerce.controller

import ecommerce.auth.AdminOnly
import ecommerce.dto.ActiveMemberStat
import ecommerce.dto.TopProductStat
import ecommerce.repository.CartStatRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class StatsController(
    private val jdbc: CartStatRepository,
) {
    @AdminOnly
    @GetMapping("/top-products")
    fun getTopProducts(): ResponseEntity<List<TopProductStat>> {
        val stats = jdbc.findTop5MostAddedProductsInLast30Days()
        return ResponseEntity.ok(stats)
    }

    @AdminOnly
    @GetMapping("/active-members")
    fun getActiveMembers(): ResponseEntity<List<ActiveMemberStat>> {
        val stats = jdbc.findActiveMembersInLast7Days()
        return ResponseEntity.ok(stats)
    }
}
