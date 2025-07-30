package ecommerce.service

import ecommerce.domain.NewMember
import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.dto.mapper.MemberMapper
import ecommerce.entity.Member
import ecommerce.exception.LoginFailedException
import ecommerce.exception.MemberAlreadyExistsException
import ecommerce.exception.MemberInsertFailedException
import ecommerce.exception.RetrievalFailedException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val repository: MemberRepository,
    private val jwtProvider: JwtProvider,
) {
    fun registerByEmail(request: MemberRequest): TokenResponse {
        val member = registerNewMember(NewMember(request.email, request.password))
        val token = jwtProvider.createToken(member)
        return MemberMapper.toResponse(token)
    }

    private fun registerNewMember(registerMember: NewMember): Member {
        if (repository.existsByEmail(registerMember.email)) {
            throw MemberAlreadyExistsException("Email ${registerMember.email} already exists")
        }

        val id =
            repository.insert(registerMember)
                ?: throw MemberInsertFailedException("Failed to insert member with email ${registerMember.email}")

        return repository.findById(id)
            ?: throw RetrievalFailedException("Member with ID $id could not be retrieved after insertion")
    }

    fun loginByEmail(request: MemberRequest): TokenResponse {
        val member = findMemberByEmailOrFail(request.email)
        validatePasswordOrFail(request.password, member.password)

        val token = jwtProvider.createToken(member)
        return MemberMapper.toResponse(token)
    }

    private fun findMemberByEmailOrFail(email: String): Member {
        if (!repository.existsByEmail(email)) {
            throw LoginFailedException()
        }
        return repository.findByEmail(email)
            ?: throw RetrievalFailedException("Member with email $email does exist, but could not be retrieved")
    }

    private fun validatePasswordOrFail(
        requestPassword: String,
        actualPassword: String,
    ) {
        if (!repository.matches(requestPassword, actualPassword)) {
            throw LoginFailedException()
        }
    }
}
