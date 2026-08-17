package com.jecfalo.palermus_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PalermusApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PalermusApiApplication.class, args);
	}

}
