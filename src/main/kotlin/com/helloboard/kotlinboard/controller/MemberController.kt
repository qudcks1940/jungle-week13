package com.helloboard.kotlinboard.controller

import com.helloboard.kotlinboard.authority.TokenInfo
import com.helloboard.kotlinboard.domain.CustomUser
import com.helloboard.kotlinboard.dto.BaseResponse
import com.helloboard.kotlinboard.dto.LoginDto
import com.helloboard.kotlinboard.dto.MemberRequestDto
import com.helloboard.kotlinboard.dto.MemberResponseDto
import com.helloboard.kotlinboard.service.*
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/member")
@RestController
class MemberController(
    private val memberService: MemberService
) {
    // 회원가입
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid memberRequestDto: MemberRequestDto): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberRequestDto)
        return BaseResponse(message = resultMsg)
    }

    // 로그인
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<MemberResponseDto> {
        val userId = (SecurityContextHolder
            .getContext()
            .authentication
            .principal as CustomUser)
            .userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    @PutMapping("/info")
    fun saveMyInfo(@RequestBody @Valid memberRequestDto: MemberRequestDto):
            BaseResponse<Unit> {
        val userId = (SecurityContextHolder
            .getContext()
            .authentication
            .principal as CustomUser)
            .userId
        memberRequestDto.id = userId
        val resultMsg: String = memberService.saveMyInfo(memberRequestDto)
        return BaseResponse(message = resultMsg)
    }

    @DeleteMapping("/info")
    fun login(): BaseResponse<TokenInfo> {
        val userId = (SecurityContextHolder
            .getContext()
            .authentication
            .principal as CustomUser)
            .userId
        val resultMsg = memberService.deleteMyInfo(userId)
        return BaseResponse(message = resultMsg)
    }

}