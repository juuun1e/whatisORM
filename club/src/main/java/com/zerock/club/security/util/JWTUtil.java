package com.zerock.club.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;


//JWT 1) 인증에 성공했을 때 JWT문자열을 만들어서 클라이언트에게 전송
//    2) 클라이언트가 보낸 토큰의 값을 검증하는 경우에 사용

//JWTUtil => 스프링 환경이 아닌 곳에서 사용할 수 있도록 유틸리티 클래스로 설계
@Log4j2
public class JWTUtil {

  private String secretKey = "zerock12345678";

  //1month
  private long expire = 60 * 24 * 30;

  //JWT Token생성 역할
  public String generateToken(String content) throws Exception{

    //문자열 자체를 알면 누구든 API를 사용할 수 잇다는 문제 발생 => 만료기간(expire)설정, secretKey를 이용하여 Signature생성
    return Jwts.builder()
        .setIssuedAt(new Date())
        .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
        //.setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()))
        .claim("sub",content) //sub라는 이름을 가지는 Claim : 사용자 이메일 주소를 입력해주어 나중에 사용할 수 있도록 구성
        .signWith(SignatureAlgorithm.HS512, secretKey.getBytes(StandardCharsets.UTF_8))
        .compact();
  }

  //인코딩된 문자열에서 원하는 값을 추출하는 용도로 작성 == 문자열 검증 역할
  //예를 들어, 만료기간 지난 것이라면 Exception발생 + sub으로 보관된 이메일 반환
  public String validateAndExtract(String tokenStr) throws Exception {

    String contentValue = null;
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
          .parseClaimsJws(tokenStr)
          .getBody();

      contentValue = claims.getSubject();


      log.info("======================================");


    }catch(Exception e){
      e.printStackTrace();
      log.error(e.getMessage());
      contentValue = null;
    }
    return contentValue;
  }
}
