package ecommerce.controller

import ecommerce.dto.MemberStatsDto
import ecommerce.dto.ProductStatsDto
import ecommerce.service.StatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/statistics")
class StatisticsController(private val statService: StatService){
    @GetMapping("/top-products")
    fun topProducts(): ResponseEntity<List<ProductStatsDto>> {
        val stats = statService.getTop5ProductsInThePast30Days()
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/active-members")
    fun activeMembers(): ResponseEntity<List<MemberStatsDto>> {
        val stats = statService.getActiveMembersInThePast7Days()
        return ResponseEntity.ok(stats)
    }
}
