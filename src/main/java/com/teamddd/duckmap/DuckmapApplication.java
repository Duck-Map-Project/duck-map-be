package com.teamddd.duckmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@ConfigurationPropertiesScan
@OpenAPIDefinition(servers = {@Server(url = "https://duckmap.shop", description = "duckmap")})
@SpringBootApplication
public class DuckmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuckmapApplication.class, args
		);
	}

}
