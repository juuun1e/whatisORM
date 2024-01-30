package org.zerock.ex2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//jpa라이브러리가 추가되었기에 자동으로 이에 관련된 설정은 추가되었지만
//구체적인 값(url)이 지정되지 않아 발생하는 문제
// ===> 해결하기 위해 oracleDB를 위한 jdbc드라이버, 프로젝트 내 oracle설정 필요
@SpringBootApplication
public class Ex2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex2Application.class, args);
	}

}
