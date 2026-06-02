package com.lms.librarymangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.lms")
@EnableJpaRepositories(basePackages = "com.lms.repository")
@EntityScan(basePackages = "com.lms.Model")
@EnableAsync
public class LibrarymangementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarymangementApplication.class, args);
	}

}
