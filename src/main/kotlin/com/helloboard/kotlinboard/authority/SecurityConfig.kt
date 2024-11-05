package com.helloboard.kotlinboard.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic{it.disable()}
            // cross side request 어쩌고
            // 웹 취약점을 공격하는 방법
            .csrf{it.disable()}
            // 우리는 토큰을 사용함.
            .sessionManagement{it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            // 인증 검증을 할 필요가 없는 api는 회원가입.
            // 그래서 설정을 해준거임.
            // anyRequest는 위의 url을 제외한 모든 요청
            .authorizeHttpRequests{
                it.requestMatchers("/api/member/signup","/api/member/login").anonymous()
                    .requestMatchers("/api/member/**").hasRole("MEMBER")
                    .anyRequest().permitAll()
            }

            // 필터를 만들기만 하면 안되고, 우리가 만든 지정을 해줘야함.
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        // 새롭게 커스텀한 걸 빌드해주는 것.
        return http.build()
    }


    // 비밀번호를 어떻게 암호화할건지 Bean으로 넘겨준다.
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()


}