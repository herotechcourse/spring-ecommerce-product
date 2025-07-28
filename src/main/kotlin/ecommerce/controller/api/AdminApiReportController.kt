package ecommerce.controller.api

import ecommerce.annotation.LoginMember
import ecommerce.domain.Member
import ecommerce.repository.reportDTO.MemberCartActivityDto
import ecommerce.repository.reportDTO.ProductCartCountDto
import ecommerce.service.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/reports")
class AdminApiReportController(private val reportService: ReportService) {
    @GetMapping("/top-products")
    fun findTop5MostAddedProductsInLast30Days(
        @LoginMember admin: Member,
        @RequestParam(defaultValue = "30") days: Int,
    ): ResponseEntity<List<ProductCartCountDto>> {
        println("Admin ${admin.userName} (ID: ${admin.userId}) is requesting top products in the last $days days report.")
        val topProducts = reportService.findTop5MostAddedProductsInLast30Days(days)
        return ResponseEntity.ok(topProducts)
    }

    @GetMapping("/members-added-to-cart")
    fun getMembersWhoAddedItemsInLast7Days(
        @LoginMember admin: Member,
        @RequestParam(defaultValue = "7") days: Int,
    ): ResponseEntity<List<MemberCartActivityDto>> {
        println("Admin ${admin.userName} (ID: ${admin.userId}) is requesting members who added items in the last $days days report.")
        val members = reportService.getMembersWhoAddedItemsInLast7Days(days)
        return ResponseEntity.ok(members)
    }
}
