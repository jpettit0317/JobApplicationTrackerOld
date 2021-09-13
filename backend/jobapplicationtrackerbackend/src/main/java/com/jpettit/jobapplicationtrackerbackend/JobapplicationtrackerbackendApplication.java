package com.jpettit.jobapplicationtrackerbackend;

import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class JobapplicationtrackerbackendApplication {
	public static ProjectEnvironment environment;

	public static void main(String[] args) {
		environment = ProjectEnvironmentReader.getEnvironment(args[0]);
		SpringApplication.run(JobapplicationtrackerbackendApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*"); // for /** means all mapping URL, and * for all domain
			}
		};
	}
}
