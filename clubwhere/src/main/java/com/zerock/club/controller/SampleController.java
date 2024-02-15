package com.zerock.club.controller;

//import com.zerock.where.security.dto.WhereAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll(){

        log.info(".....exAll.....");

    }

    @GetMapping("/member") //@AuthenticationPrincipal어노테이션을 사용하여 로그인된 사용자 정보를 확인
    public void exMember(/*@AuthenticationPrincipal WhereAuthMemberDTO AuthMember*/){

        log.info(".....exMember.....");
        log.info("===================");
     //   log.info(AuthMember);

    }


   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin(){

        log.info(".....exAdmin......");

    }

    //파라미터 값 이용하기
    //사용자 중에서 특별히 정해진 사용자만이 해당 메서드를 실행하고 싶은 경우
    //PreAuthorize의 value표현식은 #과 같은 특별한 기호나 authenticaion 같은 내장변수 이용 가능
    //user95 사용자만 접근 가능하게 만들고 싶은 경우의 설정
/*    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq\"user95@zerock.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal WhereAuthMemberDTO clubAuthMember){

        log.info(".....exMemberOnly......");
        log.info(clubAuthMember);

        return "/sample/admin";
    }*/
}
