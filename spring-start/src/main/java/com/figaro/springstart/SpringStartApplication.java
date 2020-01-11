package com.figaro.springstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class SpringStartApplication {

	@RequestMapping("/")
	String home() {
		return "Trading Plattform Database API";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringStartApplication.class, args);
	}

}
