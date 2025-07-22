package ecommerce.controller

import ecommerce.dto.RegisterRequest
import ecommerce.dto.RegisterResponse
import ecommerce.entity.User
import ecommerce.repository.UserRepository
import ecommerce.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse> {
        val userID = userService.register(request.email, request.password)

        // TODO: Add JWT
        val dummyToken = "TODO-JWT"

        return ResponseEntity.ok(RegisterResponse(token = dummyToken))
    }
}

//TODO: delete as only for manual testing of user creation
@RestController
@RequestMapping("/debug")
class DebugController(private val userRepository: UserRepository) {

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<User>> {
        val users = userRepository.getAll()
        return ResponseEntity.ok(users)
    }
}
