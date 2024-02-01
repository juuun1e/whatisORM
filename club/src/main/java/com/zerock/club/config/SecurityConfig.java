package com.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//모든 시큐리티 관련 설정이 추가되는 부분
@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    // 가장 먼저 설정이 필요한 클래스 : PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 인증처리의 핵심 Authentication Manager - 실제 동작은 UserDetailsService의 구현체(InMemory)가 담당
    // InMemoryUserDetailsManager - 필요한 사용자 정보를 구현하기에 간단한 테스트 진행 용도로 적합
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        UserDetails user = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1111"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

}
