package ecommerce.controller

import ecommerce.annotation.LoginMember
import ecommerce.annotation.Protected
import ecommerce.dto.MemberDTO
import ecommerce.model.Member
import ecommerce.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/members")
@RestController
class MemberController(private val memberService: MemberService) {
    @PostMapping
    fun register(
        @RequestBody memberDTO: MemberDTO,
    ): ResponseEntity<Void> {
        val savedMember = memberService.register(memberDTO)
        return ResponseEntity.created(URI.create("api/members/" + savedMember.id)).body(null)
    }

    @Protected
    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long,
        @LoginMember loggedMember: Member,
    ): ResponseEntity<Member> {
        val member = memberService.validateId(id)
        if (member.id != loggedMember.id) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(member)
    }
}
