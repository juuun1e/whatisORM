package com.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Security Config 파일에 빈으로 등록
@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;

    //문자열로 패턴을 입력받는 생성자가 추가 됨
    public ApiCheckFilter(String pattern){
        this.antPathMatcher=new AntPathMatcher();
        this.pattern=pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("REQUESTURI: " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern,request.getRequestURI()));

        //문자열로 패턴을 입력받음
        if (antPathMatcher.match(pattern,request.getRequestURI())){

            log.info("====================APICheckFilter=======================");
            log.info("====================APICheckFilter=======================");
            log.info("====================APICheckFilter=======================");

            //checkAuthHeader() - 'Authorization'이라는 헤더의 값을 확인하고 boolean타입의 결과를 반환
            boolean checkHeader = checkAuthHeader(request);

            if(checkHeader){
                filterChain.doFilter(request,response);
                return;
            }
            return;
    }
        //다음 필터의 단계로 넘어가기 위한 역할
        filterChain.doFilter(request,response);
    }

    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader)) {
            log.info("Authorization exist: " + authHeader);
            if (authHeader.equals("12345678")) {
                checkResult = true;
            }
        }

        return checkResult;
    }
}
