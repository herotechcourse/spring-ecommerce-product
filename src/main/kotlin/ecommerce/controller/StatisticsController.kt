package ecommerce.controller

import ecommerce.dto.MemberStatsDto
import ecommerce.dto.ProductStatsDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/statistics")
class StatisticsController(private val statService: StatService){
    @GetMapping("/top-products")
    fun topProducts(): ResponseEntity<ProductStatsDto> {
        val stats = statService.topProducts()
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/active-members")
    fun activeMembers(): ResponseEntity<List<MemberStatsDto>> {
        val stats = statService.activeMembers()
        return ResponseEntity.ok(stats)
    }
}
