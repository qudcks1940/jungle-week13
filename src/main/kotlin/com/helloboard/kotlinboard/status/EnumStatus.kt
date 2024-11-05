package com.helloboard.kotlinboard.status

enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리."),
    ERROR("에러 발생.")
}

enum class ROLE{
    MEMBER
}