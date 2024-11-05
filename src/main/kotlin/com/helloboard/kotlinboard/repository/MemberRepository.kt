package com.helloboard.kotlinboard.repository

import com.helloboard.kotlinboard.domain.Member
import com.helloboard.kotlinboard.domain.MemberRole
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByLoginId(loginId: String): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long>{
    fun deleteMemberRolesByMember(member: Member)
}