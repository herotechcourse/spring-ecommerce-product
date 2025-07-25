package ecommerce.controller

import ecommerce.dto.TopProductStats
import ecommerce.service.AdminStatsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/stats")
class AdminStatsController(
    private val adminStatsService: AdminStatsService,
) {
    @GetMapping("/top-products")
    fun getTopProducts(
        // @AdminOnly member: AdminOnly,
    ): ResponseEntity<List<TopProductStats>> {
        val productStats = adminStatsService.getTopProducts()
        return ResponseEntity.ok(productStats)
    }
}
