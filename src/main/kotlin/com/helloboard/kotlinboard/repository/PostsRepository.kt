package com.helloboard.kotlinboard.repository

import com.helloboard.kotlinboard.domain.Posts
import org.springframework.data.jpa.repository.JpaRepository

interface PostsRepository:JpaRepository<Posts, Long> {
    fun findAllByOrderByCreatedDateDesc(): List<Posts>
}