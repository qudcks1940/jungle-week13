package com.helloboard.kotlinboard.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.helloboard.kotlinboard.domain.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class MemberRequestDto(
    var id: Long?,

    @field:NotBlank
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @JsonProperty("nickname")
    private val _nickname: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message="영문, 숫자, 특수문자를 포함한 8~20자리로 입력."
    )
    @JsonProperty("password")
    private val _password: String?,
){
    val loginId: String
        get() = _loginId!!
    val nickname: String
        get() = _nickname!!
    val password: String
        get() = _password!!

    fun toEntity(): Member =
        Member(id, loginId, nickname, password)
}

data class LoginDto(
    @field:NotBlank
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,
){
    val loginId: String
        get() = _loginId!!
    val password: String
        get() = _password!!
}

data class MemberResponseDto(
    val id: Long,
    val loginId: String,
    val nickname: String,
)
