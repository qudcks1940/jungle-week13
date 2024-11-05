package com.helloboard.kotlinboard.service

import com.helloboard.kotlinboard.domain.Posts
import com.helloboard.kotlinboard.repository.PostsRepository
import com.helloboard.kotlinboard.dto.PostsCreateRequestDto
import com.helloboard.kotlinboard.dto.PostsUpdateRequestDto
import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class PostsServiceTest {
    private lateinit var postsService: PostsService
    private lateinit var postsRepository: PostsRepository

    @BeforeEach
    fun setup() {
        postsRepository = mock()
        postsService = PostsService(postsRepository)

    }

    @Test
    fun `전체 게시글 조회 테스트 성공`() {
        // given
        val posts = listOf(
            Posts(
                id = 1L,
                title = "test title 1",
                content = "test content 1",
                createdDate = LocalDateTime.now(),
                createdBy = "User1"
            ),
            Posts(
                id = 2L,
                title = "test title 2",
                content = "test content 2",
                createdDate = LocalDateTime.now(),
                createdBy = "User2"
            )
        )

        // getPosts메서드의 리턴값이 List<Posts>이기 때문에
        // thenReturn의 인자 값은 List형태가 들어가야함.
        `when`(postsRepository.findAll()).thenReturn(posts)

        // when
        val result = postsService.getPosts()

        // then
        assertEquals(posts.size, result.size) // 사이즈 확인
        assertIterableEquals(posts, result) // 리스트 내 요소 확인
        assertEquals(posts[0].title, result[0].title) // 첫 번째 게시글의 title 확인
        assertEquals(posts[1].createdBy, result[1].createdBy) // 두 번째 게시글의 createdBy 확인
    }


    @Test
    fun `한 개의 게시글 조회 테스트 성공`() {

        // given
        val postId: Long = 1L

        val post =
            Posts(
                id = 1L,
                title = "test title",
                content = "test content",
                createdDate = LocalDateTime.now(),
                createdBy = "아아아",
            )

        `when`(postsRepository.findById(postId)).thenReturn(Optional.of(post))

        // when
        val result = postsService.getPost(postId)

        // then
        assertEquals(result.id, post.id)
    }

    @Test
    fun `게시글 생성 테스트 성공`() {

        // given
        val postsCreateRequest =
            PostsCreateRequestDto(
                title = "test title",
                content = "test content",
            )

        val post =
            Posts.of(
                title = "test title",
                content = "test content",
            )

        `when`(postsRepository.save(any(Posts::class.java))).thenReturn(post)


        // when
        val result = postsService.insertPost(postsCreateRequest)


        // then
        assertEquals(result.title, postsCreateRequest.title)
        assertEquals(result.content, postsCreateRequest.content)
    }

    @Test
    fun `한 개의 게시글 수정 테스트 성공`() {

        // given
        val postId: Long = 1L

        val postsUpdateRequest =
            PostsUpdateRequestDto(
                title = "test",
//                content = "test content",
            )

        val post = Posts(
            id = postId,
            title = "test title",
            content = "test content",
            createdDate = LocalDateTime.now(),
            createdBy = "아아아",
        )

        `when`(postsRepository.findById(postId)).thenReturn(Optional.of(post))

        println("수정 이전 post.title : " + post.title)
        println("수정 이전 post.content : " + post.content)
        println("수정 이전 postUpdateRequest.content : " + postsUpdateRequest.content)
        // when & then
        postsService.updatePost(postId,postsUpdateRequest)

        println("수정 이후 post.title : " + post.title)
        println("수정 이후 post.content : " + post.content)
        println("수정 이후 postUpdateRequest.content : " + postsUpdateRequest.content)


        assertEquals(post.title, postsUpdateRequest.title)
//        assertEquals(post.content, postsUpdateRequest.content)
    }

    @Test
    fun `비밀번호가 틀려서 한 개의 게시글 수정 테스트 실패`() {

        // given
        val postId: Long = 1L

        val postsUpdateRequest =
            PostsUpdateRequestDto(
                title = "updated test title",
                content = "updated test content",
            )

        val post = Posts(
            id = postId,
            title = "test title",
            content = "test content",
            createdDate = LocalDateTime.now(),
            createdBy = "아아아",
        )

        `when`(postsRepository.findById(postId)).thenReturn(Optional.of(post))


        println("수정 전 post.tile: " + post.title)
        println("수정 전 post.content: " + post.content)

        // when & then
        assertThrows(BadRequestException::class.java) {
            postsService.updatePost(postId, postsUpdateRequest)
        }

        println("수정 후 post.tile: " + post.title)
        println("수정 후 post.content: " + post.content)

    }

    @Test
    fun `한 개의 게시글 삭제 테스트 성공`() {

        // given
        val postId: Long = 1L

        val post = Posts(
            id = postId,
            title = "test title",
            content = "test content",
            createdDate = LocalDateTime.now(),
            createdBy = "아아아",
        )

        `when`(postsRepository.findById(postId)).thenReturn(Optional.of(post))

        // when
        val result = postsService.deletePost(postId)

        // then
        verify(postsRepository, times(1)).findById(postId)
        verify(postsRepository, times(1)).delete(post)
        verify(postsRepository).delete(post)
    }

    @Test
    fun `비밀번호가 틀려서 한 개의 게시글 삭제 테스트 실패`() {

        // given
        val postId: Long = 1L

        val post = Posts(
            id = postId,
            title = "test title",
            content = "test content",
            createdDate = LocalDateTime.now(),
            createdBy = "아아아",
        )


        `when`(postsRepository.findById(postId)).thenReturn(Optional.of(post))

        // when & then
        assertThrows(BadRequestException::class.java) {
            postsService.deletePost(postId)
        }

        verify(postsRepository, times(1)).findById(postId)
        verify(postsRepository, never()).delete(any())
    }

}