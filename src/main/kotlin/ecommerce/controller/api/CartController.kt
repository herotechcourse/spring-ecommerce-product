package ecommerce.controller.api

import ecommerce.dao.JdbcCartDAO
import ecommerce.dto.CartForm
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import ecommerce.model.CartItem
import ecommerce.model.Member
import ecommerce.ui.LoginMember
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val jdbcCartDAO: JdbcCartDAO,
) {
    @GetMapping
    fun viewCart(
        @LoginMember member: Member,
    ): ResponseEntity<List<CartItem>> {
        val memberId = member.id ?: throw InternalServerErrorException("auth failed")
        val cartItems = jdbcCartDAO.getCartItemsByMemberId(memberId)
        return ResponseEntity.ok(cartItems)
    }

    @PostMapping
    fun addToCart(
        @RequestBody @Valid cartForm: CartForm,
        @LoginMember member: Member,
    ): ResponseEntity<String> {
        val memberId = member.id ?: throw InternalServerErrorException("auth failed")
        jdbcCartDAO.addItemToCart(memberId, cartForm.productId, cartForm.quantity)
        return ResponseEntity.ok(MESSAGE_ADD_SUCCESS)
    }

    @DeleteMapping("/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        @LoginMember member: Member,
    ): ResponseEntity<String> {
        val memberId = member.id ?: throw InternalServerErrorException("auth failed")
        val affectedRows = jdbcCartDAO.removeItemFromCart(memberId, productId)
        when (affectedRows) {
            1 -> return ResponseEntity.ok().body(MESSAGE_REMOVE_SUCCESS)
            0 -> throw NotFoundException("Product not found in the cart - Product ID: $productId")
            else -> throw InternalServerErrorException("Unexpected delete on item in cart - Product ID: $productId")
        }
    }

    @PutMapping("/{productId}")
    fun updateQuantity(
        @PathVariable productId: Long,
        @RequestBody @Valid cartForm: CartForm,
        @LoginMember member: Member,
    ): ResponseEntity<String> {
        val memberId = member.id ?: throw InternalServerErrorException("auth failed")
        val affectedRows = jdbcCartDAO.updateItemQuantityInCart(memberId, productId, cartForm.quantity)
        when (affectedRows) {
            1 -> return ResponseEntity.ok().body(MESSAGE_UPDATE_SUCCESS)
            0 -> throw NotFoundException("Product not found in the cart - Product ID: $productId")
            else -> throw InternalServerErrorException("Unexpected update on item in cart - Product ID: $productId")
        }
    }

    companion object {
        const val MESSAGE_ADD_SUCCESS = "Item added to cart"
        const val MESSAGE_REMOVE_SUCCESS = "Item removed from cart"
        const val MESSAGE_UPDATE_SUCCESS = "Item updated in cart"
    }
}
