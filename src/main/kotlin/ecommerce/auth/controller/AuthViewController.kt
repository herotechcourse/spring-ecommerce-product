package ecommerce.auth.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/auth")
class AuthViewController {
    @GetMapping("/signup")
    fun displaySignUpPage(): String {
        return "signUp"
    }

    @GetMapping("/login")
    fun displayLogInPage(): String {
        return "login"
    }
}
