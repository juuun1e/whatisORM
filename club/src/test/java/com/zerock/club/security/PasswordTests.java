package com.zerock.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

//BCryptPasswordEncoder를 이용하는 암호화 -> 문자열로 알아보기 어려움
//테스트 코드로 미리 어떤 값들을 사용할 수 있는지 확인해두는 것이 좋음
//테스트 코드 작성 이유 : BCryptPasswordEncoder의 동작을 확인하기 위한 용도
@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //실행할 때마다 매번 다른 결과가 만들어지고, match에 대한 결과는 true
    @Test
    public void testEncode(){

        String password = "1111";

        String enPw = passwordEncoder.encode(password);

        System.out.println("enPw = " + enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);

        System.out.println("matchResult = " + matchResult);

    }
}
