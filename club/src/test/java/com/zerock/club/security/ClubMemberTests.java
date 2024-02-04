package com.zerock.club.security;

import com.zerock.club.entity.ClubMember;
import com.zerock.club.entity.ClubMemberRole;
import com.zerock.club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
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

    //left outer join으로 처리되도록, 권한도 함께 로딩
    // ㄴ> 조인 수행 시 먼저 표기된 좌측 테이블에 해당하는 데이터를 먼저 읽은 후, 나중 표기된 우측 테이블에서 join 대상 데이터를 읽어옴
    // ㄴ> Table A와 B가 있을 때 Table A가 기준이 됨, B와 비교해서 B의 join컬럼에서 같은 값이 있을 때 해당 데이터를 가져오고,
    // B의 join컬럼에서 같은 값이 없는 경우에는 B테이블에서 가져오는 컬럼들은 NULL값으로 채움
    @Test
    public void testRead(){

        Optional<ClubMember> result = clubMemberRepository.findByEmail("user95@zerock.org",false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);

    }
}
