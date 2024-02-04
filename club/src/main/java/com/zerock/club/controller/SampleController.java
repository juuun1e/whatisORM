package com.zerock.club.controller;

import com.zerock.club.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {
    // 로그인 하지 않은 사용자도 접근할 수 있는
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
    @GetMapping("/admin")
    public void exAdmin(){

        log.info(".....exAdmin.....");

    }
}
