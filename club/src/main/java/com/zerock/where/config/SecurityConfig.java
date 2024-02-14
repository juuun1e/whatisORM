package com.zerock.where.config;

import com.zerock.where.security.filter.ApiCheckFilter;
import com.zerock.where.security.handler.ClubLoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

//모든 시큐리티 관련 설정이 추가되는 부분
@Configuration //자바 기반의 설정 클래스를 정의할 때 사용, 스프링 컨테이너는 이 클래스를 빈으로 등록함.
@EnableWebSecurity //웹 보안 활성화 됨, 주로 전역적인 웹 보안 구성을 활성화하는데 사용
@EnableMethodSecurity //메소드 수준의 보안을 활성화하는 데 사용(@PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter)
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
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(clubLoginSuccessHandler())) // OAuth2 로그인 성공 시 처리
                .rememberMe(Customizer.withDefaults())
                .addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class); // 필터의 동작 순서 조절
        return http.build();
    }

    @Bean
    public ClubLoginSuccessHandler clubLoginSuccessHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }


    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*");
    }
}
