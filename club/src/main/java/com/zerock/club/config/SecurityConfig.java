package com.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

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
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails user = User.builder()
//                .username("user1")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
// -> ClubUserDetailsService가 빈으로 등록되면 자동으로 UserDetailsService로 인식하기 때문에 더 이상 사용하지 않음

    //인가가 필요한 리소스 설정 방법 : 1)Security Config 설정을 통해 패턴 지정, 2)어노테이션 이용해 접근 제한
    //filterChain()을 작성하여 세밀한 인증/인가에 대한 처리 추가 - HttpSecurity API를 이용하여 특정한 경로 설정 가능
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("------------filterChain-----------");
        http
                .authorizeRequests(authorizeRequests ->   authorizeRequests
                                        .requestMatchers("/sample/all").permitAll() //모든 사용자에게 허가
                                        .requestMatchers("/sample/member").hasRole("USER")
                )
                .formLogin(withDefaults()) // 인증, 인가 시 로그인 화면
                .csrf(csrf -> csrf.disable()) //CSRF(Cross Site Request Forgery) - 비활성화
                .logout((logout) -> logout
                        .permitAll())
                .oauth2Login(withDefaults());
        return http.build();
    }

}
