package ecommerce.controller

import ecommerce.annotation.AdminOnly
import ecommerce.annotation.LoginMember
import ecommerce.annotation.Protected
import ecommerce.dto.AddToCartRequest
import ecommerce.dto.DeleteCartItemRequest
import ecommerce.dto.MemberResponse
import ecommerce.dto.TopProductStatResponse
import ecommerce.model.CartItem
import ecommerce.model.Member
import ecommerce.model.Role
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/cart")
@RestController
class CartController(private val cartService: CartService) {
    @Protected
    @PostMapping()
    fun addToCart(
        @RequestBody body: AddToCartRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartItem> {
        val saved = cartService.addProductToCart(member.id, body.productId, 1)
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
    @DeleteMapping()
    fun deleteCartItem(
        @RequestBody body: DeleteCartItemRequest,
        @LoginMember member: Member,
    ): ResponseEntity<Void> {
        val deleted = cartService.removeItemFromCart(member.id, body.productId)
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
