package ecommerce.controller.admin

import ecommerce.dto.stats.ActiveMemberResponse
import ecommerce.dto.stats.TopProductStats
import ecommerce.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(private val adminService: AdminService) {
    @GetMapping("/stats/top-products")
    fun getTopProducts(): ResponseEntity<List<TopProductStats>> {
        val topProducts = adminService.getTop5MostAddedProducts()
        return ResponseEntity.ok(topProducts)
    }

    @GetMapping("/stats/active-members")
    fun getActiveMembers(): ResponseEntity<List<ActiveMemberResponse>> {
        val activeMembers = adminService.getRecentlyActiveMembers()
        return ResponseEntity.ok(activeMembers)
    }
}
