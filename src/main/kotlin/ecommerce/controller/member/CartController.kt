package ecommerce.controller.member

import ecommerce.annotations.LoginMember
import ecommerce.dto.cartProduct.CartProductResponseDTO
import ecommerce.dto.user.UserDTO
import ecommerce.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/member/cart")
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("")
    fun getCartItems(
        @LoginMember user: UserDTO,
    ): ResponseEntity<List<CartProductResponseDTO>> {
        val products = cartService.getCartProducts(user.id)
        return ResponseEntity.ok(products)
    }

    @PostMapping("/{id}")
    fun addProduct(
        @LoginMember user: UserDTO,
        @PathVariable("id") productID: Long,
    ): ResponseEntity<String> {
        val id = cartService.addProductToCart(user.id, productID)
        return ResponseEntity.created(URI.create("/cart/$id")).body("Product added to cart")
    }

    @DeleteMapping("/{id}")
    fun removeProduct(
        @LoginMember user: UserDTO,
        @PathVariable("id") productID: Long,
    ): ResponseEntity<String> {
        cartService.removeProductFromCart(user.id, productID)
        return ResponseEntity.noContent().build()
    }
}
