package com.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

//security의 user클래스 상속 -> 생성자 호출할 수 있는 DTO와 유사한 클래스 구성
//DTO역할을 수행하는 클래스인 동시에 스프링시큐리티에서 인증/인가 작업에 사용 가능
@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

  //password는 부모 클래스를 사용하기에 별도의 멤버 변수로 선언하지 않음
  private String email;

  private String password;

  private String name;

  private boolean fromSocial;

  private Map<String,Object> attr;

  //소셜로그인 하는 경우 이메일 주소 대신 사용자번호 출력 => 파라미터 null
  //OAuth2User 타입을 ClubAuthMemberDTO타입으로 사용할 수 있도록 처리해줘야 함
  //-> OAuth2User 인터페이스를 구현하도록 수정
  public ClubAuthMemberDTO(
      String username,
      String password,
      boolean fromSocial,
      Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr){
    this(username, password, fromSocial, authorities);
    this.attr = attr ;
  }
  public ClubAuthMemberDTO(
      String username,
      String password,
      boolean fromSocial,
      Collection<? extends GrantedAuthority> authorities){

    super(username, password, authorities);
    this.email = username;
    this.password = password;
    this.fromSocial = fromSocial;
  }

  @Override
  public Map<String,Object> getAttributes(){
    return this.attr;
  }

}
