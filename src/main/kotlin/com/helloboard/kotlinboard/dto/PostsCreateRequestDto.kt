package com.helloboard.kotlinboard.dto

import org.springframework.data.annotation.CreatedBy
import java.time.LocalDateTime


// 자바의 record와 같은 개념
data class PostsCreateRequestDto(
    val title: String,
    val content: String,
)
