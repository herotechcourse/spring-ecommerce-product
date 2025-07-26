package ecommerce.cart.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cart")
class CartViewController {

    @GetMapping("/")
    fun displayCart(
    ): String {
        return "cart"
    }
}
