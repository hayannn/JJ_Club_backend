package com.jjclub.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//메인 클래스
@EnableJpaAuditing
@SpringBootApplication
public class authApplication {

	public static void main(String[] args) {
		SpringApplication.run(authApplication.class, args);
	}

}
