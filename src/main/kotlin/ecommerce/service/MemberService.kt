package ecommerce.service

import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.dto.mapper.MemberMapper
import ecommerce.entity.Member
import ecommerce.exception.CanNotInsertWithKeyHolderException
import ecommerce.exception.LoginFailedException
import ecommerce.exception.MemberAlreadyExistsException
import ecommerce.exception.RetrievalFailedException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val repository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun registerByEmail(request: MemberRequest): TokenResponse {
        val member = registerNewMember(request)

        val token = jwtTokenProvider.createToken(member)
        return MemberMapper.toResponse(token)
    }

    private fun registerNewMember(request: MemberRequest): Member {
        if (repository.existsByEmail(request.email)) {
            throw MemberAlreadyExistsException("Email ${request.email} already exists")
        }

        val id =
            repository.insert(request)
                ?: throw CanNotInsertWithKeyHolderException("Failed to insert member with email ${request.email}")

        return findMemberByIdOrFail(id)
    }

    fun findMemberByIdOrFail(id: Long): Member {
        return repository.findById(id)
            ?: throw RetrievalFailedException("Member with ID $id could not be retrieved after insertion")
    }

    fun loginByEmail(request: MemberRequest): TokenResponse {
        val member = findMemberByEmailOrFail(request.email)
        validatePasswordOrFail(request.password, member.password)

        val token = jwtTokenProvider.createToken(member)
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
