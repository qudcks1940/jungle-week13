package com.helloboard.kotlinboard.dto

import java.time.LocalDateTime

// 전체 값들을 client에 보내주면 안되고,
// 보여줘야하는 값들만 보여주기
data class PostsResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdDate: LocalDateTime,
    val createdBy: String,
)
