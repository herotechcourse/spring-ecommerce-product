package ecommerce.controller

import ecommerce.dto.cart.AddToCartRequest
import ecommerce.dto.cart.UpdateQuantityRequest
import ecommerce.exception.NotFoundException
import ecommerce.model.Cart
import ecommerce.repository.CartRepository
import ecommerce.repository.ProductRepository
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
import java.net.URI
import java.time.LocalDateTime

@RequestMapping("/api")
@RestController
class CartController(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) {
    @GetMapping("/cart-items")
    fun getCartItems(request: HttpServletRequest): List<Cart> {
        val userId = request.getAttribute("userId") as Long
        return cartRepository.findByUserId(userId)
    }

    @PostMapping("/cart-items")
    fun addToCart(
        @Valid @RequestBody addToCartRequest: AddToCartRequest,
        request: HttpServletRequest,
    ): ResponseEntity<Cart> {
        val userId = request.getAttribute("userId") as Long

        productRepository.findById(addToCartRequest.productId)

        val existingCart = cartRepository.findByUserIdAndProductId(userId, addToCartRequest.productId)

        val cart =
            if (existingCart != null) {
                val updatedCart = existingCart.copy(quantity = existingCart.quantity + addToCartRequest.quantity)
                cartRepository.update(updatedCart)
            } else {
                val newCart =
                    Cart(
                        memberId = userId,
                        productId = addToCartRequest.productId,
                        quantity = addToCartRequest.quantity,
                        addedAt = LocalDateTime.now(),
                    )
                cartRepository.save(newCart)
            }

        return ResponseEntity.created(URI.create("/api/cart-items")).body(cart)
    }

    @PutMapping("/cart-items/{productId}")
    fun updateQuantity(
        @PathVariable productId: Long,
        @Valid @RequestBody updateRequest: UpdateQuantityRequest,
        request: HttpServletRequest,
    ): Cart {
        val userId = request.getAttribute("userId") as Long

        productRepository.findById(productId)

        val existingCart =
            cartRepository.findByUserIdAndProductId(userId, productId)
                ?: throw NotFoundException("Item not found in cart")

        val updatedCart = existingCart.copy(quantity = updateRequest.quantity)
        return cartRepository.update(updatedCart)
    }

    @DeleteMapping("/cart-items/{productId}")
    fun removeFromCart(
        @PathVariable productId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Unit> {
        val userId = request.getAttribute("userId") as Long
        cartRepository.deleteByUserIdAndProductId(userId, productId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/cart-items")
    fun clearCart(request: HttpServletRequest): ResponseEntity<Unit> {
        val userId = request.getAttribute("userId") as Long
        cartRepository.deleteByUserId(userId)
        return ResponseEntity.noContent().build()
    }
}
