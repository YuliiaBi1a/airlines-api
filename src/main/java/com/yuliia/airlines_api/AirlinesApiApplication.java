package com.yuliia.airlines_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AirlinesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlinesApiApplication.class, args);
	}

}
