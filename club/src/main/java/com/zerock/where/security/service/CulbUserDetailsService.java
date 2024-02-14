package com.zerock.where.security.service;

import com.zerock.where.entity.ClubMember;
import com.zerock.where.repository.ClubMemberRepository;
import com.zerock.where.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

//ClubMember가 DTO타입으로 처리된 가장 큰 이유 : 사용자 정보를 가져오는 핵심적인 역할을 하는 UserDetailsService 인터페이스 때문
//스프링시큐리티의 구조엣서 인증을 담당하는 AuthenticationManager는 내부적으로 UserDetailsService를 호출해 사용자 정보를 가져옴
@Service //자동으로 스프링에서 빈으로 처리될 수 있게
@Log4j2
@RequiredArgsConstructor
public class CulbUserDetailsService implements UserDetailsService {

  // ClubMemberRepository 연동을 위해 주입받을 수 있는 구조로 변경s
  private final ClubMemberRepository clubMemberRepository;


  //주어진 이메일(username)을 사용하여 ClubMember를 찾고, 찾은 정보로 DTO생성하여 반환
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    log.info("ClubUserDetailsService loadUserByUsername" + username);

    //username이 실제로 CulbMember에서는 email을 의미하기 때문에 이를 사용해서 findByEmail을 호출
    Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

    //사용자가 존재하지 않다면 NotFoundException처리
    if(result.isEmpty()){
      throw new UsernameNotFoundException("Check Email or Social");
    }

    ClubMember clubMember = result.get();

    log.info("=========================");
    log.info(clubMember);

    //ClubMember를 UserDetail타입으로 처리하기 위해 변환
    ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
        clubMember.getEmail(),
        clubMember.getPassword(),
        clubMember.isFromSocial(),
        clubMember.getRoleSet().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
            //ClubMemberRole은 시큐리티에서 사용하는 SimpleGrantedAuthority로 변환, "ROLE_" 접두어 추가
    );

    clubAuthMember.setName(clubMember.getName());
    clubAuthMember.setFromSocial(clubMember.isFromSocial());

    return clubAuthMember;
  }
}
