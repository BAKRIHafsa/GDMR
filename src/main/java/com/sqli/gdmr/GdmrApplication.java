package com.sqli.gdmr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GdmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdmrApplication.class, args);
	}

}
