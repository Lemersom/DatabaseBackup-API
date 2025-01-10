package com.example.databasebackup;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class DatabaseBackupApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseBackupApplication.class, args);
	}

}
