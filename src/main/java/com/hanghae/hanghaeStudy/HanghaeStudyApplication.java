package com.hanghae.hanghaeStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HanghaeStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanghaeStudyApplication.class, args);
	}

}
