package com.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

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

            //checkHeader() false를 반환하는 경우 JSONObject을 이용해 JSON데이터와 403에러 페이지를 만들어 전송
            if(checkHeader){
                filterChain.doFilter(request,response);
                return;
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                //json 리턴 및 한글 깨짐 수정.
                response.setContentType("applicaion/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code","403");
                json.put("message",message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
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
