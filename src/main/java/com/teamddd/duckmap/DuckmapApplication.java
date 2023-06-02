package com.teamddd.duckmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DuckmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuckmapApplication.class, args
		);
	}

}
