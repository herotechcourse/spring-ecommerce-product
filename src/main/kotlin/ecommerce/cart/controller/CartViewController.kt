package ecommerce.cart.controller

import ecommerce.cart.service.CartService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cart")
class CartViewController(private val cartService: CartService) {

    @GetMapping("/{memberId}")
    fun displayCart(
        @PathVariable memberId: Long,
        model: Model
    ): String {
        val cart = cartService.getOrCreateCart(memberId)
        model.addAttribute("cart", cart)
        model.addAttribute("items", cart.items)
        return "cart"
    }
}
