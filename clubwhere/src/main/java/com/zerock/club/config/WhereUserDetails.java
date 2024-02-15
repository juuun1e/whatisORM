package com.zerock.club.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zerock.club.entity.Member;
import com.zerock.club.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class WhereUserDetails implements UserDetailsService{

    @Autowired
    private MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password;
        List<GrantedAuthority> authorities = new ArrayList<>();
        Optional<Member> mem = repository.findByMemId(username, false);

        //사용자 존재하지 않으면 UsernameNotFoundException, 존재한다면 사용자 정보 기반으로 UserDetails객체를 생성
        if (mem.isEmpty()) {
            throw new UsernameNotFoundException("User details not found for the user : " + username);
        } else{
            Member member = mem.get();
            userName = member.getMemId();
            password = member.getMemPw();

            member.getRoleSet().forEach(role -> {  //SimpleGrantedAuthority를 이용하여 권한(authorities)을 추가
                authorities.add(new SimpleGrantedAuthority(role.name())); // 상수 이름의 권한으로 설정되고 있어 권한을 가져올 수 있음
            });
        }
        return new User(userName,password,authorities);
    }    //UserDetails는 User객체(Spring Security에서 제공하는 사용자 정보를 담은 클래스)를 이용하여 생성

}
