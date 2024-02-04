package com.zerock.club.security.service;

import com.zerock.club.entity.ClubMember;
import com.zerock.club.entity.ClubMemberRole;
import com.zerock.club.repository.ClubMemberRepository;
import com.zerock.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

//실제 소셜로그인 과정에서 동작하는 존재: OAuth2UserService, UserDetailsService의 OAuth버전
@Log4j2
@Service //별도 설정 없이 자동으로 스프링 빈으로 등록
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

  private final ClubMemberRepository repository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

    log.info("=======================");
    log.info("userRequest:" + userRequest);
    //userRequest:org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest@45716c57
    //OAuth2UserRequest 타입의 파라미터, OAuth2User 타입의 리턴 타입을 반환
    //기존 로그인 처리에 사용하던 파라미터나 리턴타입과 다르기에 변환해서 처리해야 함

    String clientName = userRequest.getClientRegistration().getClientName();

    log.info("clientName: " + clientName); //clientName: Google
    log.info(userRequest.getAdditionalParameters());

    OAuth2User oAuth2User = super.loadUser((userRequest));

    log.info("=====================================");
    oAuth2User.getAttributes().forEach((k,v)->{
      log.info(k+":"+v);
      // sub:105809530202238897809
      // picture:https://lh3.googleusercontent.com/a-/ALV-UjUEaRTt6qtPB2pUIdS93qY4XhdDKtXF-PKhktvZjL7m7w=s96-c
      // email:goojuuun1e@gmail.com
      // email_verified:true
    });

    //oAuth2User.getAttributes()에서 추출된 이메일 주소로 현재 데이터베이스에 있는 사용자가 아니라면 자동으로 회원가입 처리
    String email = null;

    if(clientName.equals("Google")) { //구글을 이용하는 경우
      email = oAuth2User.getAttribute("email");
    }

    log.info("EMAIL: " + email);

//    ClubMember clubMember = saveSocialMember(email);
//
//    return oAuth2User;
//
    ClubMember member = saveSocialMember(email);

    //saveSocialMember()한 결과로 나온 ClubMember로 DTO 구성
    ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
        member.getEmail(),
        member.getPassword(),
        true,
        member.getRoleSet().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
            .collect(Collectors.toList()),
        oAuth2User.getAttributes() // 수정된 부분
    );

    clubAuthMember.setName(member.getName());

    return clubAuthMember;

  }


  private ClubMember saveSocialMember(String email) {
    Optional<ClubMember> result = repository.findByEmail(email, true);

    if (result.isPresent()) {
      return result.get();
    }

    // 사용자 이름과 비밀번호가 고정 됨, --> 추후 문제될 수 있는 부분
    ClubMember clubMember = ClubMember.builder().email(email)
        .name(email)
        .password(passwordEncoder.encode("1111"))
        .fromSocial(true)
        .build();

    clubMember.addMemberRole(ClubMemberRole.USER);
    repository.save(clubMember);
    return clubMember;
  }
}
