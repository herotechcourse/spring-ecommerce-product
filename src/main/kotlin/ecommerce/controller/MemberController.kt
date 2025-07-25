package ecommerce.controller

import ecommerce.dto.mapper.MemberMapper
import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.repository.MemberRepository
import ecommerce.service.JwtTokenProvider
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val store: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @PostMapping(REGISTER_ENDPOINT)
    fun registerMember(
        @Valid @RequestBody request: MemberRequest,
    ): ResponseEntity<TokenResponse> {
        return try {
            if (store.existsByEmail(request.email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }

            val id = store.insertWithKeyholder(request)
            val member =
                requireNotNull(store.findById(id)) {
                    "Member with id $id already exists"
                }
            val token = jwtTokenProvider.createToken(member)

            ResponseEntity.status(HttpStatus.CREATED).body(MemberMapper.toResponse(token))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    companion object {
        const val REGISTER_ENDPOINT = "/api/members/register"
    }
}