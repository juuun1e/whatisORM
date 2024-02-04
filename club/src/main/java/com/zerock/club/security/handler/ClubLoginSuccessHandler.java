package com.zerock.club.security.handler;

import com.zerock.club.security.dto.ClubAuthMemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

//로그인 성공 이후의 처리를 담당하는 용도 AuthenticationSuccessHandler 인터페이스 구현
@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

  //리다이텍트 되는 현상은 RedirectStrategy 로 처리 가능
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  private PasswordEncoder passwordEncoder;

  public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder){
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
    log.info("==============================");
    log.info("onAuthenticationSuccess");

    ClubAuthMemberDTO authMember = (ClubAuthMemberDTO)authentication.getPrincipal();

    boolean fromSocial = authMember.isFromSocial();

    log.info("Need Modify Member?" + fromSocial);

    boolean passwordResult = passwordEncoder.matches("1111",authMember.getPassword());

    // 소셜로그인은 대상 URL을 다르게 지정하는 용도로 => DefaultRedirectStrategy클래스 사용
    if(fromSocial && passwordResult) {
      redirectStrategy.sendRedirect(request, response, "/member/modify?from=social");
    }
  }
}
