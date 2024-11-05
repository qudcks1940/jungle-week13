package com.helloboard.kotlinboard.service

import com.helloboard.kotlinboard.authority.JwtTokenProvider
import com.helloboard.kotlinboard.authority.TokenInfo
import com.helloboard.kotlinboard.domain.CustomUser
import com.helloboard.kotlinboard.domain.Member
import com.helloboard.kotlinboard.repository.MemberRepository
import com.helloboard.kotlinboard.domain.MemberRole
import com.helloboard.kotlinboard.repository.MemberRoleRepository
import com.helloboard.kotlinboard.dto.LoginDto
import com.helloboard.kotlinboard.dto.MemberRequestDto
import com.helloboard.kotlinboard.dto.MemberResponseDto
import com.helloboard.kotlinboard.exception.InvalidInputException
import com.helloboard.kotlinboard.status.ROLE
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    // 회원가입

    fun signUp(memberRequestDto: MemberRequestDto): String{

        // ID 중복 검사
        var member: Member? = memberRepository.findByLoginId(memberRequestDto.loginId)
        if(member != null){
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }

        member = memberRequestDto.toEntity()
        memberRepository.save(member)

        val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입 완료."

    }

    // 로그인 -> 토큰 발행
    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    // 내 정보 조회
    fun searchMyInfo(id: Long): MemberResponseDto {
        val member: Member = memberRepository.findByIdOrNull(id) ?:
        throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        return member.toDto()
    }

    fun saveMyInfo(memberRequestDto: MemberRequestDto): String{
        val member = memberRequestDto.toEntity()
        memberRepository.save(member)
        return "수정 완료."
    }

    fun deleteMyInfo(id: Long): String{
        val member: Member = memberRepository.findByIdOrNull(id) ?:
        throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")

        memberRoleRepository.deleteMemberRolesByMember(member)
        memberRepository.delete(member)

        return "삭제 완료."
    }

    fun getCurrentName(id: Long): String {
        val member = memberRepository.findById(id).orElseThrow()
        return member.nickname
    }


}