package com.teamddd.duckmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DuckmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuckmapApplication.class, args
		);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins(
						"http://duck-map.s3-website.ap-northeast-2.amazonaws.com/",
						"http://localhost:3000");
			}
		};
	}

}
