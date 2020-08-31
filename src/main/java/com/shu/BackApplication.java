package com.shu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackApplication {
	@Bean
	public SpringUtil getSpingUtil() {
		return new SpringUtil();
	}
	public static void main(String[] args) {

		SpringApplication.run(BackApplication.class, args);
	}

}
