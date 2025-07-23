package ecommerce.auth.controller

import ecommerce.auth.model.Member
import ecommerce.auth.model.MemberDTO
import ecommerce.auth.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class MemberController(private val userService: MemberService) {
    @PostMapping("/members/register")
    fun registerUser(
        @Valid @RequestBody member: Member,
    ): ResponseEntity<MemberDTO> {
        val created = userService.create(member)
        val dto = MemberDTO.from(created)
        return ResponseEntity.created(URI("/users/${dto.id}")).body(dto)
    }
}
