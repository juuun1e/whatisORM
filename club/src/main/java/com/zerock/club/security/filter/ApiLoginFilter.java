package com.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

//특정 URL로 외부에서 로그인이 가능하도록, 로그인 성공 시 클라이언트가 Authorization헤더의 값으로 이용할 데이터를 전송할 것
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
//AbstractAuthenticationProcessingFilter - 실행문 없음 : 추상클래스로 설계

  public ApiLoginFilter(String defaultFilterProcessesUrl){

    super(defaultFilterProcessesUrl);
  }

  //상속 : attemptAuthentication() 추상메서드와 문자열도 패턴을 받는 생성자가 반드시 필요
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    log.info("===================ApiLoginFilter======================");
    log.info("attemptAuthentication");

    //파라미터가 있어야만 동작 가능
    String email = request.getParameter("email");
    String pw =  request.getParameter("pw");

    //AuthenticationManager의 Authenticate() : 파라미터와 리턴타입 모두 Authentication타입
    //파라미터로 전송하는 Authentication타입의 객체로 Token 생성 -- email,pw를 받아 실제 인증처리
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(email,pw);

    return getAuthenticationManager().authenticate(authToken);
  }

  //AbstractAuthenticationProcessingFilter클래스에 성공 처리 메서드를 재정의해서 구현
  //successfulAuthentication()의 마지막 파라미터 : 성공한 사용자 인증정보를 가지고 있는 Authentication객체
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
    throws IOException, ServletException {

    log.info("---------------------ApiLoginFilter--------------------------");
    log.info("successfulAuthentication: " + authResult);

    log.info(authResult.getPrincipal());

  }
}
