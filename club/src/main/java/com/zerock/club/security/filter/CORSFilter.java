package com.zerock.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) //우선순위 설정 : 가장 먼저 동작
public class CORSFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException{

    response.setHeader("Access-Control-Allow-Origin","*");
    response.setHeader("Access-Control-Allow-Credentials","true");
    response.setHeader("Access-Control-Allow-Metods","*");
    response.setHeader("Access-Control-Max-Age","3600");
    response.setHeader("Access-Control-Allow-Header","Origin, X-Requested-with,Content-Type,Accept,Key,Authorization");

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
      response.setStatus(HttpServletResponse.SC_OK);
    }else{
      filterChain.doFilter(request,response);
    }

  }
}
