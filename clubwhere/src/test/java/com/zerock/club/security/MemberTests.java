package com.zerock.club.security;

import com.zerock.club.entity.Member;
import com.zerock.club.entity.MemberRole;
import com.zerock.club.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        // 1-80까지는 USER만 지정
        //81-90까지는 USER,MANAGER
        //91-100 USER,MANAGER,ADMIN

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .memId("user"+i+"@zerock.org")
                    .memName(" 사용자"+i)
                    .memNickname(" 사용자"+i)
                    .fromSocial(false)
                    .memPw(passwordEncoder.encode("1111"))
                    .build();
            //default role
            member.addMemberRole((MemberRole.USER));

            if(i>80){
                member.addMemberRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }


    @Test
    public void testRead(){

         Optional<Member> result = memberRepository.findByMemId("user95@zerock.org",false);

        Member member = result.get();

        System.out.println(member);

    }
}