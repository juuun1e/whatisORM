package com.zerock.club.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

//AbstractAuthenticationProcessingFilter에는 setAuthenticationFailureHandler()로 인증 실패 처리 지정 가능
@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception)
      throws IOException, ServletException {

    log.info("...............login fail Handler................");
    log.info(exception.getMessage());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //json리턴, 인증 실패 시 401상태코드 반환
    response.setContentType("application/json;charset=utf-8");
    JSONObject json = new JSONObject();
    String message = exception.getMessage();
    json.put("code","401");
    json.put("message",message);

    PrintWriter out = response.getWriter();
    out.print(json);
  }

}
