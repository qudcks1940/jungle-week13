package com.helloboard.kotlinboard.authority

data class TokenInfo(
    val grantType: String,
    val accessToken: String,
)