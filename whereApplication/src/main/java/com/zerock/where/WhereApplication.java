package com.zerock.where;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //BaseEntity 를 이용하기 위해 추가
public class WhereApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereApplication.class, args);
	}

}
