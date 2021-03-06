package com.bowlingsystem.bowlingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class BowlingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BowlingsystemApplication.class, args);
	}

}
