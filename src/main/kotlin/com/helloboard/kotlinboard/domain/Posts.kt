package com.helloboard.kotlinboard.domain

import com.fasterxml.jackson.databind.ser.Serializers.Base
import com.helloboard.kotlinboard.service.MemberService
import jakarta.persistence.*
import org.hibernate.cache.internal.NaturalIdCacheKey
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime


@Entity
@Table()
class Posts(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, updatable = false)
    val memberId: Long,

    @Column(nullable = false, length = 255)
    var title: String,

    @Column(nullable = false, length = 6553)
    var content: String,

//    @CreatedDate
    @Column(name =  "created_datetime", nullable = false, updatable = false)
    var createdDate: LocalDateTime,

//    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: String,

    )
//    : BaseEntity()
{

    companion object {
        fun of(memberId: Long, title: String, content: String, createdBy: String):
                Posts = Posts(
            memberId = memberId,
            title = title,
            content = content,
            createdDate = LocalDateTime.now(),
            createdBy = createdBy,
        )
    }

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateContent(content: String) {
        this.content = content
    }

}
