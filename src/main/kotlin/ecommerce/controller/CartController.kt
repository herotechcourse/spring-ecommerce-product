package ecommerce.controller

import ecommerce.annotation.AdminOnly
import ecommerce.annotation.LoginMember
import ecommerce.annotation.Protected
import ecommerce.dto.MemberResponse
import ecommerce.dto.TopProductStatResponse
import ecommerce.model.CartItem
import ecommerce.model.Member
import ecommerce.model.Role
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/cart")
@RestController
class CartController(private val cartService: CartService) {
    @Protected
    @PostMapping("add/{productId}")
    fun addToCart(
        @LoginMember member: Member,
        @PathVariable("productId") productId: Long,
    ): ResponseEntity<CartItem> {
        val saved = cartService.addProductToCart(member.id, productId, 1)
        return ResponseEntity.ok(saved)
    }

    @Protected
    @GetMapping
    fun getCartItems(
        @LoginMember member: Member,
    ): ResponseEntity<List<CartItem>> {
        val items = cartService.getCartItemsForMember(member.id)
        if (items.isEmpty()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(items)
    }

    @Protected
    @DeleteMapping("/{productId}")
    fun deleteCartItem(
        @LoginMember member: Member,
        @PathVariable productId: Long,
    ): ResponseEntity<Void> {
        val deleted = cartService.removeItemFromCart(member.id, productId)
        return if (deleted != null) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Protected
    @GetMapping("/top5")
    fun getTopCartItems(): ResponseEntity<List<TopProductStatResponse>> {
        val items = cartService.getTop5AddedProducts()
        if (items.isEmpty()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(items)
    }

    @Protected
    @AdminOnly
    @GetMapping("/active-members")
    fun getActiveMembers(
        @LoginMember member: Member,
    ): ResponseEntity<List<MemberResponse>> {
        if (member.role != Role.ADMIN) {
            return ResponseEntity.status(401).build()
        }
        val items = cartService.getRecentActiveMembersInTheLast7Days()
        if (items.isEmpty()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(items)
    }
}
