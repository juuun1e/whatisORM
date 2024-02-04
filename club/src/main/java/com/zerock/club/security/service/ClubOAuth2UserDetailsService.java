package com.zerock.club.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

//실제 소셜로그인 과정에서 동작하는 존재: OAuth2UserService, UserDetailsService의 OAuth버전
@Log4j2
@Service //별도 설정 없이 자동으로 스프링 빈으로 등록
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

    log.info("=======================");
    log.info("userRequest:" + userRequest);
    //userRequest:org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest@45716c57
    //OAuth2UserRequest타입의 파라미터, OAuth2User타입의 리턴 타입을 반환
    //기존 로그인 처리에 사용하던 파라미터나 리턴타입과 다르기에 변환해서 처리해야 함

    String clientName = userRequest.getClientRegistration().getClientName();

    log.info("clientName: " + clientName);
    log.info(userRequest.getAdditionalParameters());

    OAuth2User oAuth2User = super.loadUser((userRequest));

    log.info("=======================");
    oAuth2User.getAttributes().forEach((k,v)->{
      log.info(k+":"+v);
    });
    return oAuth2User;
  }
}
