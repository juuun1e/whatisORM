package com.zerock.club.config;

import com.zerock.club.security.handler.WhereLoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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

        //인증관리자 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //get 인증관리자
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //AbstractAuthenticationProcessingFilter는 반드시 AuthenticationManager가 필요
        http.authenticationManager(authenticationManager);


        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").hasRole("ADMIN")
                        .requestMatchers("/sample/member").hasRole("USER")
                        .requestMatchers("/notices", "/contact", "/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
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
}
