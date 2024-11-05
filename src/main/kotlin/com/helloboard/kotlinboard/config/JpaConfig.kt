//package com.helloboard.kotlinboard.config
//
//import com.helloboard.kotlinboard.service.MemberService
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.domain.AuditorAware
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing
//import java.util.*
//
//
//@Configuration
//@EnableJpaAuditing
//class JpaConfig (
//    private val memberService: MemberService,
//){
//
//    @Bean
//    fun auditorAware(): AuditorAware<String> =
//        AuditorAware {
//            Optional.of(memberService.getCurrentName())
//        }
//
//}