package com.spring.boot.timeoffapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
//@EnableSwagger2
public class TimeOffAppApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TimeOffAppApplication.class, args);
	}
	
	@Bean
	public OpenAPI openAPIConfig() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		Info info = new Info();
		info.title("Time Off Module")
		.description("This API handles time offs types, and time off requests made from employees, so that "
				+ "users with defined roles can manage and access them."
				+ " \n\n\n\n CONTACT: giovanna.borges@theksquare.com.mx")
	
		.version("v1.0.0");
		return info;
	}

}
