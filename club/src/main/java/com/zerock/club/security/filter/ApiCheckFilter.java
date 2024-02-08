package com.zerock.club.security.filter;

import com.zerock.club.security.util.JWTUtil;
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
    //Athorization 헤더 메시지를 통해 JWT를 확인하도록 생성자를 통해 주입
    private JWTUtil jwtUtil;

    //문자열로 패턴을 입력받는 생성자가 추가 됨
    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher=new AntPathMatcher();
        this.pattern=pattern;
        this.jwtUtil=jwtUtil;
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
        //doFilter() - 다음 필터의 단계로 넘어가기 위한 역할
        filterChain.doFilter(request,response);
    }

    //특정 API를 호출하는 클라이언트에서는 다른 서버나 Application으로 실행되기에 쿠키나 세션 활용 불가
    // => Request를 전송할 때 Http헤더 메시지에 특별한 값을 지정해서 전송 : 파악한 헤더 값으로 사용자 요청이 정상적인지 파악
    //'Authorization'이라는 헤더의 값을 확인(검증)하고 boolean타입의 결과를 반환
        private boolean checkAuthHeader(HttpServletRequest request) {


        boolean checkResult = false;

        //Authorization 헤더 확인 : 클라이언트의 HTTP 요청에서 "Authorization" 헤더 값 추출 (인증 토큰)
        String authHeader = request.getHeader("Authorization");

        //"Authorization" 헤더 메시지 - 일반적인 경우 Basic, JWT 이용할 때는 Bearer 사용
        //Bearer 토큰 확인 : "Authorization" 헤더 값이 존재하고, 그 값이 "Bearer "로 시작하는지 확인
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist: " + authHeader);
            try{ //"Bearer " 다음의 토큰 값을 추출하고, jwtUtil.validateAndExtract 메서드로 토큰 유효성 검사
                 //주어진 JWT 토큰을 파싱, 만료 여부 등을 확인한 후, 토큰에 포함된 이메일 주소를 반환하거나 예외처리
                String email = jwtUtil.validateAndExtract((authHeader.substring(7)));
                log.info("validate result : "+email);
                checkResult = email.length() > 0;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        //최종적으로 checkResult 값을 반환 (클라이언트의 요청이 유효한 경우 true, 그렇지 않으면 false)
        return checkResult;
    }
}
