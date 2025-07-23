package ecommerce.service

import ecommerce.dto.RegistrationRequest
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val memberRepository: MemberRepository, repository: MemberRepository) {
    fun registerMember(request: RegistrationRequest): String {
        if (memberRepository.existsByEmail(request.email)) {
            throw MemberEmailAlreadyExistsException("Email already exists: ${request.email}")
        }
        val created = memberRepository.save(request)
        return "toDo"
    }
}
