package com.helloboard.kotlinboard.controller

import com.helloboard.kotlinboard.domain.CustomUser
import com.helloboard.kotlinboard.domain.Posts
import com.helloboard.kotlinboard.dto.BaseResponse
import com.helloboard.kotlinboard.dto.PostsCreateRequestDto
import com.helloboard.kotlinboard.dto.PostsResponse
import com.helloboard.kotlinboard.service.PostsService
import com.helloboard.kotlinboard.dto.PostsUpdateRequestDto
import com.helloboard.kotlinboard.service.MemberService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/posts")
class PostsController(
    private val postsService: PostsService,
    private val memberService: MemberService
) {

    @PostMapping()
    fun insertPost(

        // RequestBody는 객체를 길게 받아야할 때 사용
        // RequestParam은 짧은 것들 받을 때 사용
        @RequestBody postsCreateRequestDto: PostsCreateRequestDto,
        ): PostsResponse{
        val memberId = (SecurityContextHolder
            .getContext()
            .authentication
            .principal as CustomUser)
            .userId

        return postsService.insertPost(memberId, postsCreateRequestDto)
    }

    @GetMapping("/{postId}")
    fun getPost(
        @PathVariable postId: Long
    ): PostsResponse = postsService.getPost(postId)

    @GetMapping()
    fun getPosts():List<Posts> = postsService.getPostsDesc()

    @PatchMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody postsUpdateRequestDto: PostsUpdateRequestDto,
    ) :BaseResponse<Unit> {
        val memberId = (SecurityContextHolder
            .getContext()
            .authentication
            .principal as CustomUser)
            .userId

        val resultMsg: String = postsService.updatePost(postId, memberId, postsUpdateRequestDto)
        return BaseResponse(message = resultMsg)
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
    ) :BaseResponse<Unit> {
        val memberId = (SecurityContextHolder
            .getContext()
            .authentication
            .principal as CustomUser)
            .userId


        val resultMsg: String = postsService.deletePost(memberId, postId)
        return BaseResponse(message = resultMsg)
    }
}