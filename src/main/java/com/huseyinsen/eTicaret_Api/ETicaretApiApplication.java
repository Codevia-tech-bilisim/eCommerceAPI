package com.huseyinsen.eTicaret_Api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.huseyinsen"})
@SpringBootApplication
@ComponentScan(basePackages = {"com.huseyinsen"})
@EnableJpaRepositories(basePackages = {"com.huseyinsen"})
public class ETicaretApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ETicaretApiApplication.class, args);
	}

}
