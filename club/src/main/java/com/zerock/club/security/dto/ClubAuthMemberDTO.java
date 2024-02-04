package com.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//security의 user클래스 상속 -> 생성자 호출할 수 있는 DTO와 유사한 클래스 구성
//DTO역할을 수행하는 클래스인 동시에 스프링시큐리티에서 인증/인가 작업에 사용 가능
@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User {

  //password는 부모 클래스를 사용하기에 별도의 멤버 변수로 선언하지 않음
  private String email;

  private String name;

  private boolean fromSocial;

  public ClubAuthMemberDTO(
      String username,
      String password,
      boolean fromSocial,
      Collection<? extends GrantedAuthority> authorities){

    super(username, password, authorities);
    this.email = username;
    this.fromSocial = fromSocial;
  }

}
