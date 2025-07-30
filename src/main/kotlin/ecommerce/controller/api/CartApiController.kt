package ecommerce.controller.api

import ecommerce.annotation.LoginMember
import ecommerce.domain.Cart
import ecommerce.domain.CartItem
import ecommerce.domain.Member
import ecommerce.dto.cart.AddToCartRequest
import ecommerce.dto.cart.CartResponse
import ecommerce.dto.cartItem.CartItemResponse
import ecommerce.service.CartService
import ecommerce.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartApiController(
    private val cartService: CartService,
    private val productService: ProductService,
) {
    private fun CartItem.toCartItemResponse(): CartItemResponse? {
        val product = productService.getProductById(this.productId)

        return product.let {
            CartItemResponse(
                productId = it.id,
                productName = it.name,
                quantity = this.quantity,
                price = it.price,
            )
        }
    }

    private fun buildCartResponse(
        cart: Cart,
        memberId: Long,
    ): CartResponse {
        val cartItemsDomain = cartService.getCartItems(memberId)

        val cartItemResponses = cartItemsDomain.mapNotNull { it.toCartItemResponse() }

        val totalPrice = cartItemResponses.sumOf { it.price * it.quantity }
        val totalQuantity = cartItemResponses.sumOf { it.quantity }

        return CartResponse(
            id = cart.id,
            memberId = memberId,
            items = cartItemResponses,
            totalPrice = totalPrice,
            totalQuantity = totalQuantity,
        )
    }

    @GetMapping
    fun getCart(
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val cartEntity = cartService.getCart(member.userId)
        val cartResponse = buildCartResponse(cartEntity, member.userId)
        return ResponseEntity.ok().body(cartResponse)
    }

    @PostMapping
    fun addProductToCart(
        @Valid @RequestBody request: AddToCartRequest,
        @LoginMember member: Member,
    ): ResponseEntity<CartResponse> {
        val updatedCartEntity =
            cartService.addProductToCart(
                memberId = member.userId,
                productId = request.productId,
                quantity = request.quantity,
            )
        val cartResponse = buildCartResponse(updatedCartEntity, member.userId)
        return ResponseEntity.ok().body(cartResponse)
    }

    @DeleteMapping("/{id}")
    fun deleteProductFromCart(
        @LoginMember member: Member,
        @PathVariable("id") id: Long,
    ): ResponseEntity<Void> {
        cartService.removeProductFromCart(member.userId, id)
        return ResponseEntity.noContent().build()
    }
}
