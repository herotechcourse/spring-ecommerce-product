package ecommerce.controller.api

import ecommerce.auth.AuthorizationExtractor
import ecommerce.auth.BearerAuthorizationExtractor
import ecommerce.dao.JdbcCartDAO
import ecommerce.dto.CartForm
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import ecommerce.model.CartItem
import ecommerce.service.AuthService
import jakarta.servlet.http.HttpServletRequest
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
    private val authService: AuthService,
) {
    private val authorizationExtractor: AuthorizationExtractor<String> = BearerAuthorizationExtractor()

    private fun getMemberId(request: HttpServletRequest): Long {
        val token = authorizationExtractor.extract(request)
        return authService.findMemberByToken(token).id
    }

    @GetMapping
    fun viewCart(): ResponseEntity<List<CartItem>> {
        val cartItems = jdbcCartDAO.getCartItemsByMemberId(MEMBER_ID)
        return ResponseEntity.ok(cartItems)
    }

    @PostMapping
    fun addToCart(
        @RequestBody @Valid cartForm: CartForm,
    ): ResponseEntity<String> {
        jdbcCartDAO.addItemToCart(MEMBER_ID, cartForm.productId, cartForm.quantity)
        return ResponseEntity.ok().body(MESSAGE_ADD_SUCCESS)
    }

    @DeleteMapping("/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
    ): ResponseEntity<String> {
        val affectedRows = jdbcCartDAO.removeItemFromCart(MEMBER_ID, productId)
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
    ): ResponseEntity<String> {
        val affectedRows = jdbcCartDAO.updateItemQuantityInCart(MEMBER_ID, productId, cartForm.quantity)
        when (affectedRows) {
            1 -> return ResponseEntity.ok().body(MESSAGE_UPDATE_SUCCESS)
            0 -> throw NotFoundException("Product not found in the cart - Product ID: $productId")
            else -> throw InternalServerErrorException("Unexpected update on item in cart - Product ID: $productId")
        }
    }

    companion object {
        const val MEMBER_ID = 1L
        const val MESSAGE_ADD_SUCCESS = "Item added to cart"
        const val MESSAGE_REMOVE_SUCCESS = "Item removed from cart"
        const val MESSAGE_UPDATE_SUCCESS = "Item updated in cart"
    }
}
