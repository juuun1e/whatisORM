package com.zerock.club.security;

import com.zerock.club.security.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//jwt검증에 문제가 없다면 최종적으로 ApiLoginFilter와 ApiCheckFilter에 적용
public class JWTTests {

  private JWTUtil jwtUtil;

  @BeforeEach
  public void testBefore() {
    System.out.println("...........testBefore.............");
    jwtUtil = new JWTUtil();
  }

  //JWT문자열 생성
  @Test
  public void testEncode() throws Exception {
    String email = "user95@zerock.org";

    String str = jwtUtil.generateToken(email);

    System.out.println(str);
  }

  //generateToken()에 대한 검증
  @Test
  public void testVaildate() throws Exception {
    String email = "user95@zerock.org";

    String str = jwtUtil.generateToken(email);

    Thread.sleep(5000);

    String resultEmail = jwtUtil.validateAndExtract(str);

    System.out.println(resultEmail);
  }
}
