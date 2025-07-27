package ecommerce.service

import ecommerce.dto.MemberDTO
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class MemberService (private val memberRepository: MemberRepository)  {
    fun validateEmail(email: String?)  {
        require(email != null) {"Email cannot be empty."}

        val existingMember = memberRepository.findByEmail(email)

        if (existingMember != null) {
            throw IllegalArgumentException("$email is already registered.")
        }

        require(!email.isBlank()) {"Email cannot be empty."}
        require("@" in email && ".com" in email) {"Please provide a valid email address"}
    }

    fun validatePassword(password: String?)  {
        require(password != null) {"Password cannot be empty."}
        require(!password.isBlank()) {"Password cannot be empty."}
        require(password.count() >= 4) {"Password with a minimum of 4  and maximum of 8 characters long."}
        require(password.count() <= 8) {"Password with a minimum of 4  and maximum of 8 characters long."}
    }

    fun validateId(id: Long) : Member {
        val member = memberRepository.findById(id)
        require(member != null) {"Member with id $id does not exist."}
        return member
    }

    fun register(memberDTO: MemberDTO): Member {
        validateEmail(memberDTO.email)
        validatePassword(memberDTO.password)
        return memberRepository.insert(memberDTO.toEntity())
    }
}
