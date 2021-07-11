package com.weatherObserver.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableSwagger2
@ComponentScan(basePackages = { "com.weatherObserver" })
@EnableJpaRepositories(basePackages = "com.weatherObserver.repository")
@EntityScan(basePackages = "com.weatherObserver.entity")
public class WeatherObserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherObserverApplication.class, args);
	}
}
