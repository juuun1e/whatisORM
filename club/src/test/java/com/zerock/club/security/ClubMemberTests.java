package com.zerock.club.security;

import com.zerock.club.entity.ClubMember;
import com.zerock.club.entity.ClubMemberRole;
import com.zerock.club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //여러 명의 회원을 추가,한 명의 회원이 여러 개의 권한을 가질 수 있도록
    @Test
    public void insertDummies(){

        // 1-80까지는 USER만 지정
        //81-90까지는 USER,MANAGER
        //91-100 USER,MANAGER,ADMIN

        IntStream.rangeClosed(1,100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user"+i+"@zerock.org")
                    .name(" 사용자"+i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            //default role
            clubMember.addMemberRole((ClubMemberRole.USER));

            if(i>80){
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }

            if(i>90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            clubMemberRepository.save(clubMember);
        });
    }
}
