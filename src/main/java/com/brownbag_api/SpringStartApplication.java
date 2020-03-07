package com.brownbag_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.security.model.data.InitDataLoaderSec;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class SpringStartApplication {

	@RequestMapping("/")
	String home() {
		return "Brownbag API";
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringStartApplication.class, args);

	}

	@Bean
	public CommandLineRunner loadData(InitDataLoaderSec loader) {
		return args -> {
			loader.createRoles();
			loader.createUsers();
		};
	}

}
