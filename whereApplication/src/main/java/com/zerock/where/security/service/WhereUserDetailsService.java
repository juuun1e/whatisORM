package com.zerock.where.security.service;

import com.zerock.where.entity.MemberUser;
import com.zerock.where.repository.MemberUserRepository;
import com.zerock.where.security.dto.AuthMemberUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class WhereUserDetailsService implements UserDetailsService {

    // ClubMemberRepository 연동을 위해 주입받을 수 있는 구조로 변경
    private final MemberUserRepository repository;


    //주어진 이메일(username)을 사용하여 ClubMember를 찾고, 찾은 정보로 DTO생성하여 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailsService loadUserByUsername" + username);

        //username이 실제로 CulbMember에서는 email을 의미하기 때문에 이를 사용해서 findByEmail을 호출
        Optional<MemberUser> result = repository.findByMemId(username, false);

        //사용자가 존재하지 않다면 NotFoundException처리
        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check Email or Social");
        }

        MemberUser member = result.get();

        log.info("=========================");
        log.info(member);

        //ClubMember를 UserDetail타입으로 처리하기 위해 변환
        AuthMemberUserDTO AuthMember = new AuthMemberUserDTO(
                member.getMemId(),
                member.getMemPw(),
                member.isFromSocial(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
                //MemberUserRole은 시큐리티에서 사용하는 SimpleGrantedAuthority로 변환, "ROLE_" 접두어 추가
        );

        AuthMember.setName(member.getMemName());
        AuthMember.setFromSocial(member.isFromSocial());

        return AuthMember;
    }
}
