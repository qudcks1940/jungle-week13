package com.helloboard.kotlinboard.service

import com.helloboard.kotlinboard.domain.Posts
import com.helloboard.kotlinboard.repository.PostsRepository
import com.helloboard.kotlinboard.dto.PostsCreateRequestDto
import com.helloboard.kotlinboard.dto.PostsResponse
import com.helloboard.kotlinboard.dto.PostsUpdateRequestDto
import com.helloboard.kotlinboard.exception.InvalidInputException
import com.helloboard.kotlinboard.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class PostsService(
    private val postsRepository: PostsRepository,
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
) {

    @Transactional(readOnly = true)
    fun getPostsDesc():List<Posts> = postsRepository.findAllByOrderByCreatedDateDesc()

    @Transactional(readOnly = true)
    fun getPost(postId:Long) : PostsResponse {
        // orElseThrow : null이면 오류를 반환하라는 함수
        // findById를 들어가보면,
        val post=postsRepository.findById(postId).orElseThrow()

        return PostsResponse(
            id = post.id,
            title = post.title,
            content = post.content,
            createdDate = post.createdDate,
            createdBy = post.createdBy,
        )
    }

    fun insertPost(
        memberId: Long, postsCreateRequestDto: PostsCreateRequestDto
    ) : PostsResponse {

        memberRepository.findById(memberId).orElseThrow()

        val posts: Posts = postsRepository.save(
            Posts.of(
                memberId,
                postsCreateRequestDto.title,
                postsCreateRequestDto.content,
                memberService.getCurrentName(memberId),
            )
        )

        return PostsResponse(
            id = posts.id,
            title = posts.title,
            content = posts.content,
            createdDate = posts.createdDate,
            createdBy = posts.createdBy,
            )
    }

    fun updatePost(
        id:Long,
        memberId: Long,
        postsUpdateRequestDto: PostsUpdateRequestDto,
    ) :String {
        val post = postsRepository.findById(id).orElseThrow()

        if(memberId != post.memberId){
            throw IllegalArgumentException("해당 게시글을 수정할 수 있는 id가 아닙니다.")
        }

        if(!postsUpdateRequestDto.title.isNullOrBlank()){
            post.updateTitle(postsUpdateRequestDto.title)
        }

        println(postsUpdateRequestDto.content)

        if(!postsUpdateRequestDto.content.isNullOrBlank()){
            println(postsUpdateRequestDto.content)
            post.updateContent(postsUpdateRequestDto.content)
        }

        println(postsUpdateRequestDto.content)

        return "수정 완료."
    }

    fun deletePost(
        memberId: Long,
        id:Long,
    ) : String{
        val post = postsRepository.findById(id).orElseThrow()

        if(memberId != post.memberId){
            throw IllegalArgumentException("해당 게시글을 삭제할 수 있는 id가 아닙니다.")
        }

        postsRepository.delete(post)
        return "삭제 완료."
    }





}