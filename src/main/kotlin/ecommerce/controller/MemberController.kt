package ecommerce.controller

import ecommerce.dto.MemberDTO
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import ecommerce.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/members")
@RestController
class MemberController (private val repository: MemberRepository, private val memberService: MemberService) {

    @PostMapping
    fun register(
        @RequestBody memberDTO: MemberDTO,
    ): ResponseEntity<Void> {
        val savedMember = memberService.register(memberDTO)
        return ResponseEntity.created(URI.create("/members/" + savedMember.id)).body(null)
    }

    @GetMapping("/{id}")
    fun get(): ResponseEntity<List<Member>> {
        if (repository.isEmptyOrNull()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(repository.findAll())
    }
}
