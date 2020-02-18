package com.brownbag_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.database.DB;
import com.brownbag_api.model.demo.Demo;


@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class SpringStartApplication {

	@RequestMapping("/")
	String home() {
		return "Trading Plattform Database API modified";
	}
	
	public static void main(String[] args) {
		DB.connect();
		DB.openSession();
		Demo.createDemoData();
//		DB.closeSession();

		SpringApplication.run(SpringStartApplication.class, args);
	}

}
