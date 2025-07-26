package ecommerce.admin.controller

import ecommerce.admin.dto.RecentMemberResponse
import ecommerce.admin.dto.TopProductResponse
import ecommerce.admin.service.AdminService
import ecommerce.auth.annotation.LoginMember
import ecommerce.member.domain.Member
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(private val adminService: AdminService) {
    @GetMapping("/top-products")
    fun getTopProducts(
        @LoginMember member: Member,
    ): ResponseEntity<List<TopProductResponse>> {
        return ResponseEntity.ok(adminService.getTopProducts())
    }

    @GetMapping("/recent-members")
    fun getRecentMembers(
        @LoginMember member: Member,
    ): ResponseEntity<List<RecentMemberResponse>> {
        return ResponseEntity.ok(adminService.getRecentMembers())
    }
}
