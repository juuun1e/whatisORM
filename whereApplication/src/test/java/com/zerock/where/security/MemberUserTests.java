package com.zerock.where.security;

import com.zerock.where.entity.MemberUser;
import com.zerock.where.entity.MemberUserRole;
import com.zerock.where.repository.MemberUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberUserTests {

    @Autowired
    private MemberUserRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        // 1-80까지는 USER만 지정
        //81-90까지는 USER,MANAGER
        //91-100 USER,MANAGER,ADMIN

        IntStream.rangeClosed(1,100).forEach(i -> {
            MemberUser member = MemberUser.builder()
                    .memId("user"+i+"@zerock.org")
                    .memName(" 사용자"+i)
                    .memNickname(" 사용자"+i)
                    .fromSocial(false)
                    .memPw(passwordEncoder.encode("1111"))
                    .build();
            //default role
            member.addMemberRole((MemberUserRole.USER));

            if(i>80){
                member.addMemberRole(MemberUserRole.MANAGER);
            }

            if(i>90){
                member.addMemberRole(MemberUserRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }


    @Test
    public void testRead(){

         Optional<MemberUser> result = memberRepository.findByMemId("user95@zerock.org",false);

        MemberUser member = result.get();

        System.out.println(member);

    }
}