package com.wes.processdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ProcessdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessdbApplication.class, args);
	}

}
