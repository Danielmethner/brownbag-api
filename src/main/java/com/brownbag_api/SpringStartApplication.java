package com.brownbag_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.brownbag_api.model.demo.Demo;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class SpringStartApplication {

	@RequestMapping("/")
	String home() {
		return "Brownbag API";
	}

	public static void main(String[] args) {

		Demo.createDemoData();

		SpringApplication.run(SpringStartApplication.class, args);
	}

}
