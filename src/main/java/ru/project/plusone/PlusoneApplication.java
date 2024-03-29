package ru.project.plusone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PlusoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlusoneApplication.class, args);
	}

	@Bean(initMethod = "init")
	DataInit dataInit() {
		return new DataInit();
	}
}



