package com.zerock.club.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

//실행문 없음 : 추상클래스로 설계
//attemptAuthenticationcn() 추상메서드와 문자열도 패턴을 받는 생성자가 반드시 필요
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

  public ApiLoginFilter(String defaultFilterProcessesUrl){
    super(defaultFilterProcessesUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    log.info("===================ApiLoginFilter======================");
    log.info("attemptAuthentication");

    //email이라는 파라미터가 있어야만 동작 가능
    String email = request.getParameter("email");
    String pw =  request.getParameter("pw");

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,pw);

    return getAuthenticationManager().authenticate(authToken);
  }

}
