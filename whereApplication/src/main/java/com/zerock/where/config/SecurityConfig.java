package com.zerock.where.config;

import com.zerock.where.security.handler.WhereLoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

//모든 시큐리티 관련 설정이 추가되는 부분
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log4j2
public class SecurityConfig {

    // 가장 먼저 설정이 필요한 클래스 : PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //인가가 필요한 리소스 설정 방법 : 1)Security Config 설정을 통해 패턴 지정, 2)어노테이션 이용해 접근 제한
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
                        .successHandler(whereLoginSuccessHandler())); // OAuth2 로그인 성공 시 처리
//                .rememberMe(Customizer.withDefaults())
//                .addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class); // 필터의 동작 순서 조절
          return http.build();
    }

    @Bean
    public WhereLoginSuccessHandler whereLoginSuccessHandler(){
        return new WhereLoginSuccessHandler(passwordEncoder());
    }

//
//    @Bean
//    public ApiCheckFilter apiCheckFilter(){
//        return new ApiCheckFilter("/notes/**/*");
//    }
}
