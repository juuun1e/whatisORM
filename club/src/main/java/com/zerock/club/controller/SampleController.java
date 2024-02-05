package com.zerock.club.controller;

import com.zerock.club.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {
    // 로그인 하지 않은 사용자도 접근할 수 있는
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll(){

        log.info(".....exAll.....");

    }

    // 로그인한 사용자만 접근 가능한
    @GetMapping("/member") //@AuthenticationPrincipal어노테이션을 사용하여 로그인된 사용자 정보를 확인
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){

        log.info(".....exMember.....");
        log.info("===================");
        log.info(clubAuthMember);

    }

    // 관리자 권한이 있는 사용자만 접근 가능한
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin(){

        log.info(".....exAdmin......");

    }

    //파라미터 값 이용하기
    //사용자 중에서 특별히 정해진 사용자만이 해당 메서드를 실행하고 싶은 경우
    //PreAuthorize의 value표현식은 #과 같은 특별한 기호나 authenticaion 같은 내장변수 이용 가능
    //user95 사용자만 접근 가능하게 만들고 싶은 경우의 설정
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq\"user95@zerock.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){

        log.info(".....exMemberOnly......");
        log.info(clubAuthMember);

        return "/sample/admin";
    }
}
